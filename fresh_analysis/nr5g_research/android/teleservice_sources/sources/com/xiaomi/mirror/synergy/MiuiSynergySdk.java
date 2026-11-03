package com.xiaomi.mirror.synergy;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.settings.search.utils.SearchContract;
import com.ot.pubsub.a.a;
import com.ot.pubsub.g.f;
import com.xiaomi.mirror.IMediaRelayCallback;
import com.xiaomi.mirror.IMirrorAdvConnectionCallback;
import com.xiaomi.mirror.IMirrorDeviceStatusCallback;
import com.xiaomi.mirror.IMirrorShareCallback;
import com.xiaomi.mirror.ISameAccountApCallback;
import com.xiaomi.mirror.MirrorDesktopInfo;
import com.xiaomi.mirror.RemoteDeviceInfo;
import com.xiaomi.mirror.ScreenInfo;
import com.xiaomi.mirror.SynergyDeviceInfo;
import com.xiaomi.mirror.util.AppUtil;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class MiuiSynergySdk {
    private static final String ACTION_MIRROR_DEVICE_CHANGED = "miui.intent.action.MIRROR_DEVICE_CHANGED";
    private static final String ACTION_MIRROR_PIN_APP_CHANGED = "miui.intent.action.PIN_APP_CHANGED";
    private static final String ACTION_MISHARE_ONEHOP = "com.miui.mishare.action.NFC_TOUCH_TRANSFER";
    private static final String EXTRA_DEVICE_ID = "device_id";
    private static final String EXTRA_DEVICE_MANUFACTURER = "device_manufacturer";
    private static final String EXTRA_DEVICE_STATE = "device_state";
    private static final String EXTRA_PIN_APP_CHANGE_ACTION = "extra_pin_app_change_action";
    private static final String EXTRA_PIN_APP_CHANGE_APP_PACKAGE_NAME = "extra_pin_app_change_app_package_name";
    private static final String EXTRA_PIN_APP_CHANGE_DEVICE_ID = "extra_pin_app_change_device_id";
    public static final String PACKAGE_MISHARE = "com.miui.mishare.connectivity";
    private static final int PIN_APP_CHANGE_ACTION_PIN_MANAGER_INIT = 3;
    private static final int PIN_APP_CHANGE_ACTION_TYPE_ADD = 1;
    private static final int PIN_APP_CHANGE_ACTION_TYPE_REMOVE = 2;
    public static final int SEND_APP_FAIL_APP_SENDING = -2;
    public static final int SEND_APP_FAIL_DEVICE_DISCONNECT = -5;
    public static final int SEND_APP_FAIL_PARAMETER_ERROR = -1;
    public static final int SEND_APP_FAIL_REMOTE_ADV_ERROR = -7;
    public static final int SEND_APP_FAIL_REMOTE_BUSY_MIPLAY = -103;
    public static final int SEND_APP_FAIL_REMOTE_BUSY_MIRROR = -102;
    public static final int SEND_APP_FAIL_REMOTE_BUSY_MISHARE = -101;
    public static final int SEND_APP_FAIL_REMOTE_BUSY_OTHERS = -100;
    public static final int SEND_APP_FAIL_REMOTE_BUSY_WORLD = -104;
    public static final int SEND_APP_FAIL_REMOTE_MAX_SCREEN_COUNT = -10;
    public static final int SEND_APP_FAIL_REMOTE_PKG_ERROR = -9;
    public static final int SEND_APP_FAIL_REMOTE_SYNC_ERROR = -8;
    public static final int SEND_APP_FAIL_REMOTE_UNKNOWN = -6;
    public static final int SEND_APP_FAIL_SELF_DEVICE = -3;
    public static final int SEND_APP_FAIL_UNKNOWN = 0;
    public static final int SEND_APP_FAIL_WAIT_TIMEOUT = -4;
    private static final int SEND_APP_SUCCESS = 1;
    public static final int SYNERGY_ERR = -1;
    public static final int SYNERGY_OK = 0;
    public static final int SYNERGY_SOFTAP_ALREADY_CONNECTED = 2;
    public static final int SYNERGY_SOFTAP_ALREADY_CONNECTING = 1;
    private static final String TAG = "MiuiSynergy";
    private static final int VALUE_DEVICE_STATE_EXIT = 2;
    private static final int VALUE_DEVICE_STATE_JOIN = 0;
    private static final int VALUE_DEVICE_STATE_UPDATE = 1;
    private MediaRelayCallback mMediaRelayCallback;
    private PinAppChangeCallBack mPinAppChangeCallBack;
    private BroadcastReceiver mPinAppReceiver;
    private BroadcastReceiver mReceiver;
    private CopyOnWriteArrayList mRemoteDeviceListeners;
    private SameAccountApCallback mSameAccountApCallback;
    private TakingPhotoManager mTakingPhotoManager;
    private final Map mRelayAppCallbackMap = new ConcurrentHashMap();
    private final Executor mExecutor = Executors.newCachedThreadPool(new NamedThreadFactory());
    private final ISameAccountApCallback mISameAccountApCallback = new ISameAccountApCallback.Stub() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.5
        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApConnectedStatusUpdate(int i, Bundle bundle) throws RemoteException {
            if (MiuiSynergySdk.this.mSameAccountApCallback != null) {
                MiuiSynergySdk.this.mSameAccountApCallback.onApConnectedStatusUpdate(i, bundle == null ? null : new SameAccountAccessPoint(bundle));
            }
        }

        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApInfoUpdate(Bundle bundle) throws RemoteException {
            if (MiuiSynergySdk.this.mSameAccountApCallback != null) {
                MiuiSynergySdk.this.mSameAccountApCallback.onApInfoUpdate(bundle == null ? null : new SameAccountAccessPoint(bundle));
            }
        }
    };

    @Deprecated
    private IRemoteDeviceListener mRemoteDeviceListener = null;
    private AdvConnectionCallbackWrapper mMirrorAdvConnectionCallback = null;
    private final IMediaRelayCallback mIMediaRelayCallback = new IMediaRelayCallback.Stub() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.13
        @Override // com.xiaomi.mirror.IMediaRelayCallback
        public void onVolumeChanged(String str, int i) throws RemoteException {
            if (MiuiSynergySdk.this.mMediaRelayCallback != null) {
                MiuiSynergySdk.this.mMediaRelayCallback.onVolumeChanged(str, i);
            }
        }
    };

    public interface AdvConnectionCallback {
        void onAdvConnectionLost();
    }

    class AdvConnectionCallbackWrapper extends IMirrorAdvConnectionCallback.Stub {
        private final WeakReference mCallback;

        @Override // com.xiaomi.mirror.IMirrorAdvConnectionCallback
        public void onLost() {
            MiuiSynergySdk.this.mMirrorAdvConnectionCallback = null;
            if (this.mCallback.get() != null) {
                ((AdvConnectionCallback) this.mCallback.get()).onAdvConnectionLost();
            }
        }

        private AdvConnectionCallbackWrapper(AdvConnectionCallback advConnectionCallback) {
            this.mCallback = new WeakReference(advConnectionCallback);
        }
    }

    public interface ChooseFileCallback {
        void onFileChosen(ClipData clipData);
    }

    class DeviceReceiver extends BroadcastReceiver {
        private DeviceReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            CopyOnWriteArrayList<IRemoteDeviceListener> copyOnWriteArrayList;
            String action = intent.getAction();
            if (action == null || !MiuiSynergySdk.ACTION_MIRROR_DEVICE_CHANGED.equals(action) || (copyOnWriteArrayList = MiuiSynergySdk.this.mRemoteDeviceListeners) == null || copyOnWriteArrayList.isEmpty()) {
                return;
            }
            int intExtra = intent.getIntExtra("device_state", -1);
            String stringExtra = intent.getStringExtra(MiuiSynergySdk.EXTRA_DEVICE_ID);
            String stringExtra2 = intent.getStringExtra(MiuiSynergySdk.EXTRA_DEVICE_MANUFACTURER);
            if (TextUtils.isEmpty(stringExtra)) {
                for (IRemoteDeviceListener iRemoteDeviceListener : copyOnWriteArrayList) {
                    if (iRemoteDeviceListener.enableHistoryListener()) {
                        iRemoteDeviceListener.onUpdate("");
                    }
                }
                return;
            }
            Log.d("MiuiSynergy", "ACTION_MIRROR_DEVICE_CHANGED received. state=" + intExtra + ",id=" + stringExtra);
            if (intExtra == 0) {
                for (IRemoteDeviceListener iRemoteDeviceListener2 : copyOnWriteArrayList) {
                    String listenManufacturer = iRemoteDeviceListener2.getListenManufacturer();
                    if (listenManufacturer == null || listenManufacturer.equals(stringExtra2)) {
                        iRemoteDeviceListener2.onFound(stringExtra);
                    }
                }
                return;
            }
            if (intExtra == 1) {
                for (IRemoteDeviceListener iRemoteDeviceListener3 : copyOnWriteArrayList) {
                    String listenManufacturer2 = iRemoteDeviceListener3.getListenManufacturer();
                    if (listenManufacturer2 == null || listenManufacturer2.equals(stringExtra2)) {
                        iRemoteDeviceListener3.onUpdate(stringExtra);
                    }
                }
                return;
            }
            if (intExtra != 2) {
                return;
            }
            for (IRemoteDeviceListener iRemoteDeviceListener4 : copyOnWriteArrayList) {
                String listenManufacturer3 = iRemoteDeviceListener4.getListenManufacturer();
                if (listenManufacturer3 == null || listenManufacturer3.equals(stringExtra2)) {
                    iRemoteDeviceListener4.onLost(stringExtra);
                }
            }
        }
    }

    final class Holder {
        private static final MiuiSynergySdk INSTANCE = new MiuiSynergySdk();

        private Holder() {
        }
    }

    public abstract class IRemoteDeviceListener {
        public boolean enableHistoryListener() {
            return false;
        }

        public String getListenManufacturer() {
            return RemoteDeviceInfo.MANUFACTURER_XIAOMI;
        }

        public abstract void onFound(String str);

        public abstract void onLost(String str);

        public abstract void onUpdate(String str);
    }

    public interface MediaRelayCallback {
        void onVolumeChanged(String str, int i);
    }

    public class MiuiSynergyException extends Exception {
        public MiuiSynergyException() {
        }

        public MiuiSynergyException(String str) {
            super(str);
        }
    }

    class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger mThreadNum;

        private NamedThreadFactory() {
            this.mThreadNum = new AtomicInteger(1);
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pool-synergyT-" + this.mThreadNum.getAndIncrement());
        }
    }

    public class Option {
        public Bitmap icon;
        private String id;
        public String title;

        public int invoke(Activity activity, Uri uri, String str) {
            MiuiSynergySdk.getInstance();
            if (MiuiSynergySdk.isMirrorNotAlive()) {
                return -1;
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uri);
            bundle.putString("extra", str);
            bundle.putString("id", this.id);
            bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
            try {
                CallMethod.doCall(activity.getContentResolver(), "openOnSynergy", null, bundle);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    class PinAppChangeReceiver extends BroadcastReceiver {
        private PinAppChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && MiuiSynergySdk.ACTION_MIRROR_PIN_APP_CHANGED.equals(action)) {
                int intExtra = intent.getIntExtra(MiuiSynergySdk.EXTRA_PIN_APP_CHANGE_ACTION, 0);
                String stringExtra = intent.getStringExtra(MiuiSynergySdk.EXTRA_PIN_APP_CHANGE_DEVICE_ID);
                String stringExtra2 = intent.getStringExtra(MiuiSynergySdk.EXTRA_PIN_APP_CHANGE_APP_PACKAGE_NAME);
                if (intExtra == 0 || ((TextUtils.isEmpty(stringExtra) || TextUtils.isEmpty(stringExtra2)) && intExtra != 3)) {
                    Log.e("MiuiSynergy", "PIN_APP_CHANGED Extra error changeAction： " + intExtra + " deviceId is empty: " + stringExtra + " packageName is empty: " + stringExtra2);
                    return;
                }
                if (MiuiSynergySdk.this.mPinAppChangeCallBack == null) {
                    Log.e("MiuiSynergy", "PIN_APP_CHANGED callback is null");
                    return;
                }
                if (intExtra == 1) {
                    MiuiSynergySdk.this.mPinAppChangeCallBack.addPinApp(stringExtra2, stringExtra);
                } else if (intExtra == 2) {
                    MiuiSynergySdk.this.mPinAppChangeCallBack.removePinApp(stringExtra2, stringExtra);
                } else if (intExtra == 3) {
                    MiuiSynergySdk.this.mPinAppChangeCallBack.pinAppManagerInit();
                }
            }
        }
    }

    public interface QueryOpenCallback {
        void onQueryResult(List list);
    }

    public interface RelayAppCallback {
        void onFailure(int i);

        void onSuccess();
    }

    public class SameAccountAccessPoint {
        private int batteryPercent;
        private boolean is5G;
        private String ssid;

        public SameAccountAccessPoint(Bundle bundle) {
            this.batteryPercent = -1;
            if (bundle == null) {
                return;
            }
            this.ssid = bundle.getString("apSsid");
            this.is5G = bundle.getBoolean("apId5G");
            this.batteryPercent = bundle.getInt("batteryPercent", -1);
        }

        public int getBatteryPercent() {
            return this.batteryPercent;
        }

        public String getSsid() {
            return this.ssid;
        }

        public boolean isIs5G() {
            return this.is5G;
        }
    }

    public interface SameAccountApCallback {
        void onApConnectedStatusUpdate(int i, SameAccountAccessPoint sameAccountAccessPoint);

        void onApInfoUpdate(SameAccountAccessPoint sameAccountAccessPoint);
    }

    public static /* synthetic */ void $r8$lambda$4wyapIkE1ILSQJBfrc77vO_Mkz4(ArrayList arrayList, Context context, GetAllPinAppListCallBack getAllPinAppListCallBack) {
        try {
            Bundle bundle = new Bundle();
            ArrayList<String> arrayList2 = new ArrayList<>();
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                arrayList2.add(((PinAppInfo) obj).getStr());
            }
            bundle.putStringArrayList(CallMethod.ARG_PIN_APP_LIST, arrayList2);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "syncPinApp", null, bundle);
            if (bundleDoCall == null) {
                Log.e("MiuiSynergy", "syncPinApp result is null ");
                return;
            }
            int i3 = bundleDoCall.getInt(a.L);
            String string = bundleDoCall.getString(a.m);
            if (i3 != 0) {
                Log.e("MiuiSynergy", "syncPinApp error code = " + i3 + " message: " + string);
                getAllPinAppListCallBack.onFail(i3, string);
                return;
            }
            ArrayList<String> stringArrayList = bundleDoCall.getStringArrayList(CallMethod.ARG_PIN_APP_LIST);
            if (stringArrayList == null) {
                getAllPinAppListCallBack.onFail(i3, string);
                return;
            }
            ArrayList arrayList3 = new ArrayList();
            int size2 = stringArrayList.size();
            while (i < size2) {
                String str = stringArrayList.get(i);
                i++;
                PinAppInfo pinAppInfoPareByString = PinAppInfo.pareByString(str);
                if (pinAppInfoPareByString != null) {
                    arrayList3.add(pinAppInfoPareByString);
                }
            }
            getAllPinAppListCallBack.onSuccess(arrayList3);
        } catch (Exception e) {
            getAllPinAppListCallBack.onFail(-1, e.getMessage());
            e.printStackTrace();
        }
    }

    public static /* synthetic */ void $r8$lambda$XiXKgUB__kaKSkPhjn4jlksrAlM(String str, String str2, String str3, String str4, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            bundle.putString(f.a.e, str2);
            bundle.putString("appName", str3);
            bundle.putString("uri", str4);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "removePinApp", null, bundle);
            if (bundleDoCall == null) {
                Log.e("MiuiSynergy", "removePinApp result is null ");
                return;
            }
            int i = bundleDoCall.getInt(a.L);
            String string = bundleDoCall.getString(a.m);
            if (i != 0) {
                Log.e("MiuiSynergy", "removePinApp error code = " + i + " message: " + string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static /* synthetic */ void $r8$lambda$mhc22NXSTqaPfIhL9EcfH5j0AS8(String str, String str2, String str3, String str4, Context context) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            bundle.putString(f.a.e, str2);
            bundle.putString("appName", str3);
            bundle.putString("uri", str4);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "addPinApp", null, bundle);
            if (bundleDoCall == null) {
                Log.e("MiuiSynergy", "addPinApp result is null ");
                return;
            }
            int i = bundleDoCall.getInt(a.L);
            String string = bundleDoCall.getString(a.m);
            if (i != 0) {
                Log.e("MiuiSynergy", "addPinApp error code = " + i + " message: " + string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static /* synthetic */ void $r8$lambda$nSRCEnEdkUOoflaf2k4RXEJgW2A(Context context, GetAllPinAppListCallBack getAllPinAppListCallBack) {
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getAllPinApp", null, new Bundle());
            if (bundleDoCall == null) {
                Log.e("MiuiSynergy", "getAllPinApp result is null ");
                return;
            }
            int i = bundleDoCall.getInt(a.L);
            String string = bundleDoCall.getString(a.m);
            if (i != 0) {
                Log.e("MiuiSynergy", "getAllPinApp error code = " + i + " message: " + string);
                getAllPinAppListCallBack.onFail(i, string);
                return;
            }
            ArrayList<String> stringArrayList = bundleDoCall.getStringArrayList(CallMethod.ARG_PIN_APP_LIST);
            if (stringArrayList == null) {
                getAllPinAppListCallBack.onFail(i, "result is null");
                return;
            }
            ArrayList arrayList = new ArrayList();
            int size = stringArrayList.size();
            int i2 = 0;
            while (i2 < size) {
                String str = stringArrayList.get(i2);
                i2++;
                PinAppInfo pinAppInfoPareByString = PinAppInfo.pareByString(str);
                if (pinAppInfoPareByString != null) {
                    arrayList.add(pinAppInfoPareByString);
                }
            }
            getAllPinAppListCallBack.onSuccess(arrayList);
        } catch (Exception e) {
            getAllPinAppListCallBack.onFail(-1, e.getMessage());
            e.printStackTrace();
        }
    }

    public static MiuiSynergySdk getInstance() {
        return Holder.INSTANCE;
    }

    public static Uri getUriFor(String str) {
        return new Uri.Builder().scheme("content").authority("com.xiaomi.mirror.callprovider").path(str).build();
    }

    public static boolean isMirrorNotAlive() {
        try {
            boolean z = false;
            Object objInvoke = Class.forName("android.app.IActivityManager$Stub").getMethod("asInterface", IBinder.class).invoke(null, (IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", String.class).invoke(null, "activity"));
            if (objInvoke != null) {
                Class[] clsArr = new Class[0];
                List<ActivityManager.RunningAppProcessInfo> list = (List) objInvoke.getClass().getMethod("getRunningAppProcesses", null).invoke(objInvoke, null);
                if (list != null) {
                    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
                        if (runningAppProcessInfo != null && AppUtil.PACKAGE_NAME_MIRROR.equalsIgnoreCase(runningAppProcessInfo.processName)) {
                            z = true;
                            break;
                        }
                    }
                }
            }
            Log.d("MiuiSynergy", "mirror isRunning" + z);
            return !z;
        } catch (Exception e) {
            Log.e("MiuiSynergy", "isMirrorAlive: ", e);
            return true;
        }
    }

    public void addPinApp(final Context context, final String str, final String str2, final String str3, final String str4) {
        if (isMirrorNotAlive()) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MiuiSynergySdk.$r8$lambda$mhc22NXSTqaPfIhL9EcfH5j0AS8(str, str2, str3, str4, context);
            }
        });
    }

    public void addRemoteDeviceListener(Context context, IRemoteDeviceListener iRemoteDeviceListener) {
        if (AppUtil.getMirrorInstallStatus(context) == 2) {
            Log.e("MiuiSynergy", "addRemoteDeviceListener: failed, mirror isn't installed");
            return;
        }
        if (this.mRemoteDeviceListeners == null) {
            this.mRemoteDeviceListeners = new CopyOnWriteArrayList();
        }
        if (this.mReceiver == null) {
            this.mReceiver = new DeviceReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_MIRROR_DEVICE_CHANGED);
            context.getApplicationContext().registerReceiver(this.mReceiver, intentFilter, 2);
        }
        this.mRemoteDeviceListeners.add(iRemoteDeviceListener);
    }

    public int chooseFileOnSynergy(Activity activity, final ChooseFileCallback chooseFileCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        final Bundle bundle = new Bundle();
        bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
        final ContentResolver contentResolver = activity.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.2
            @Override // java.lang.Runnable
            public void run() {
                Bundle bundleDoCall = CallMethod.doCall(contentResolver, "chooseFileFromSynergy", null, bundle);
                chooseFileCallback.onFileChosen(bundleDoCall != null ? (ClipData) bundleDoCall.getParcelable("clipData") : null);
            }
        });
        return 0;
    }

    public int closeMirrorFromClient(Context context) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            CallMethod.doCall(context.getContentResolver(), "closeMirrorFromClient", null, null);
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    @Deprecated
    public int closeRemoteDeviceMirror(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putString("remoteDeviceId", str);
        try {
            CallMethod.doCall(context.getContentResolver(), "closeRemoteDeviceMirror", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int closeRemoteDeviceMirror3(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putString("remoteDeviceId", str);
        bundle.putInt("method_version", 3);
        this.mRelayAppCallbackMap.remove(str);
        try {
            CallMethod.doCall(context.getContentResolver(), "closeRemoteDeviceMirror", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int connectSameAccountAp(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putString("apSsid", str);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "connectSameAccountAp", null, bundle);
            if (bundleDoCall == null) {
                return -1;
            }
            return bundleDoCall.getInt("softApState", -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void enableMirror(Context context, String str, int i) {
        if (isMirrorNotAlive()) {
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            bundle.putInt("enableWithReason", i);
            CallMethod.doCall(context.getContentResolver(), "enableMirror", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IBinder getAliveBinder(Context context) {
        Bundle bundleDoCall;
        if (isMirrorNotAlive() || (bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getAliveBinder", null, null)) == null) {
            return null;
        }
        return bundleDoCall.getBinder("binder");
    }

    public void getAllPinApp(final Context context, final GetAllPinAppListCallBack getAllPinAppListCallBack) {
        if (!isMirrorNotAlive()) {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MiuiSynergySdk.$r8$lambda$nSRCEnEdkUOoflaf2k4RXEJgW2A(context, getAllPinAppListCallBack);
                }
            });
        } else if (getAllPinAppListCallBack != null) {
            getAllPinAppListCallBack.onFail(-1, "mirror process not alive");
        }
    }

    public CallRelayService getCallRelayService(Context context) {
        Bundle bundleDoCall;
        IBinder binder;
        if (isMirrorNotAlive() || (bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getCallRelayService", null, null)) == null || (binder = bundleDoCall.getBinder("binder")) == null) {
            return null;
        }
        return new CallRelayService(binder);
    }

    public int getInt(Context context, Uri uri, int i) {
        String string;
        if (!isMirrorNotAlive() && (string = getString(context, uri, null)) != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException unused) {
            }
        }
        return i;
    }

    public String getLatestSubScreenPackageName(Context context, String str) {
        if (isMirrorNotAlive()) {
            return null;
        }
        Log.d("MiuiSynergy", "getLatestSubScreenPackageName");
        new Bundle();
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getLatestSubScreenPackageName", null, null);
            if (bundleDoCall != null) {
                return bundleDoCall.getString(a.p);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public int getMirrorAdvConnection(Context context, AdvConnectionCallback advConnectionCallback) {
        if (isMirrorNotAlive() || this.mMirrorAdvConnectionCallback != null) {
            return -1;
        }
        this.mMirrorAdvConnectionCallback = new AdvConnectionCallbackWrapper(advConnectionCallback);
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mMirrorAdvConnectionCallback.asBinder());
        try {
            return CallMethod.doCall(context.getContentResolver(), "getMirrorAdvConnection", null, bundle) != null ? 0 : -1;
        } catch (Exception unused) {
        }
    }

    public boolean getRemoteDeviceStatus(Context context, String str, int i) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            bundle.putInt("extra", i);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getRemoteDeviceStatus", null, bundle);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PackageInfo getRemotePackageInfo(Context context, String str, String str2) throws PackageManager.NameNotFoundException, MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        Bundle bundle = new Bundle();
        bundle.putString(f.a.e, str);
        Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getRemotePackageInfo", str2, bundle);
        if (bundleDoCall == null) {
            throw new MiuiSynergyException("local miui+ not support");
        }
        if (bundleDoCall.getInt(a.L) != 0) {
            throw new MiuiSynergyException(bundleDoCall.getString(a.m, ""));
        }
        PackageInfo packageInfo = (PackageInfo) bundleDoCall.getParcelable("packageInfo");
        if (packageInfo != null) {
            return packageInfo;
        }
        throw new PackageManager.NameNotFoundException(str + " not found in remote device");
    }

    public String getShareStatus(Context context) throws MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getShareStatus", null, null);
        if (bundleDoCall != null) {
            return bundleDoCall.getString(a.p);
        }
        throw new MiuiSynergyException("local miui+ not support");
    }

    public int getShowMirrorType(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", str);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getShowMirrorType", null, bundle);
            if (bundleDoCall != null) {
                return bundleDoCall.getInt(a.p);
            }
        } catch (Exception unused) {
        }
        return -1;
    }

    public ArrayList getSinkScreensInfo(Context context, String str) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getSinkScreensInfo", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            bundleDoCall.setClassLoader(ScreenInfo.class.getClassLoader());
            return bundleDoCall.getParcelableArrayList("sinkScreensInfo");
        } catch (Exception unused) {
            return null;
        }
    }

    public String getString(Context context, Uri uri, String str) {
        if (!isMirrorNotAlive()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uri);
            try {
                Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "get", null, bundle);
                if (bundleDoCall != null) {
                    return bundleDoCall.getString(a.p);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return str;
            }
        }
        return str;
    }

    public String getSubScreenAppName(Context context, String str) {
        if (isMirrorNotAlive()) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", str);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getSubScreenAppName", null, bundle);
            if (bundleDoCall != null) {
                return bundleDoCall.getString(a.p);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public List getSubScreenPackageList(Context context) {
        Log.d("MiuiSynergy", "getSubScreenPackageList");
        if (isMirrorNotAlive()) {
            return new ArrayList();
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getSubScreenPackageList", null, new Bundle());
            if (bundleDoCall == null) {
                Log.e("MiuiSynergy", "getSubScreenPackageList result is null ");
                return new ArrayList();
            }
            ArrayList<String> stringArrayList = bundleDoCall.getStringArrayList(a.p);
            Log.d("MiuiSynergy", "getSubScreenPackageList stringArrayList: " + stringArrayList);
            return stringArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public String getSubScreenPackageName(Context context, String str) {
        if (isMirrorNotAlive()) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", str);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getSubScreenPackageName", null, bundle);
            if (bundleDoCall != null) {
                return bundleDoCall.getString(a.p);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public List getSynergyDevices(Context context) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getSynergyDevices", null, null);
            bundleDoCall.setClassLoader(SynergyDeviceInfo.class.getClassLoader());
            ArrayList parcelableArrayList = bundleDoCall.getParcelableArrayList("remoteDevices");
            if (parcelableArrayList == null || parcelableArrayList.isEmpty()) {
                return null;
            }
            return parcelableArrayList;
        } catch (Exception unused) {
        }
    }

    public boolean isAgreePrivacy(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isAgreePrivacy", null, null);
            if (bundleDoCall == null) {
                bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isAgreePrivacy", null, null);
                Log.w("MiuiSynergy", "isAgreePrivacy, fix binder error, result=" + bundleDoCall);
            }
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFloatWindowShow(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isFloatWindowShow", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("isFloatWindowShow");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFreedomWindow(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isFreedomWindow", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception unused) {
        }
    }

    public boolean isP2PWorking(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isP2PWorking", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isShowMirrorFromPhone(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isShowMirrorFromPhone", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception unused) {
        }
    }

    public boolean isSplitWindow(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSplitWindow", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception unused) {
        }
    }

    public boolean isSupportMacSidecar(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSupportMacSidecar", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception unused) {
        }
    }

    public boolean isSupportSendApp(Context context, String str) {
        if (isMirrorNotAlive()) {
            return false;
        }
        return isSupportSendApp(context, str, "");
    }

    public boolean isSupportSendAppToPhone(Context context, String str) {
        if (isMirrorNotAlive()) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString(f.a.e, str);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSupportSendAppToPhone", null, bundle);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSupportTakePhoto(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        return isSupportTakePhoto(context, false);
    }

    public boolean isSynergyEnable(Context context) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSynergyEnable", null, null);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTakingPhoto(Context context, String str) throws MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", str);
        Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isTakingPhoto", null, bundle);
        if (bundleDoCall != null) {
            return bundleDoCall.getBoolean(a.p);
        }
        throw new MiuiSynergyException("current miui+ version not support");
    }

    public void launchAppFromPendingIntent(Context context, String str, PendingIntent pendingIntent, String str2, RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
                return;
            }
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CallMethod.ARG_DEVICE_ID, str);
        bundle.putParcelable("pendingIntent", pendingIntent);
        bundle.putString(f.a.e, str2);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "launchAppFromPendingIntent", null, bundle);
            int i = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
            if (relayAppCallback != null) {
                if (i == 0) {
                    relayAppCallback.onSuccess();
                } else {
                    relayAppCallback.onFailure(i);
                }
            }
        } catch (Exception unused) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
            }
        }
    }

    public void launchAppFromPendingIntentSink(Context context, String str, String str2, RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
                return;
            }
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CallMethod.ARG_DEVICE_ID, str);
        bundle.putString(f.a.e, str2);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "launchAppFromPendingIntentSink", null, bundle);
            int i = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
            if (relayAppCallback != null) {
                if (i == 0) {
                    relayAppCallback.onSuccess();
                } else {
                    relayAppCallback.onFailure(i);
                }
            }
        } catch (Exception unused) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
            }
        }
    }

    public ParcelFileDescriptor openDirect(Context context, Uri uri) throws IOException {
        if (isMirrorNotAlive()) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "openDirect", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            return (ParcelFileDescriptor) bundleDoCall.getParcelable("fileDescriptor");
        } catch (Exception e) {
            throw new IOException("open failed", e);
        }
    }

    public int openMiCloudOnSynergy(Context context) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            CallMethod.doCall(context.getContentResolver(), "openMiCloudOnSynergy", null, null);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int openOnSynergy(Activity activity, Uri uri, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        bundle.putString("extra", str);
        bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
        try {
            CallMethod.doCall(activity.getContentResolver(), "openOnSynergy", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Deprecated
    public void openRemoteDeviceMirror(Context context, String str, final RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
            }
        } else {
            final Bundle bundle = new Bundle();
            bundle.putString("remoteDeviceId", str);
            final ContentResolver contentResolver = context.getContentResolver();
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.10
                @Override // java.lang.Runnable
                public void run() {
                    Bundle bundleDoCall = null;
                    try {
                        bundleDoCall = CallMethod.doCall(contentResolver, "openRemoteDeviceMirror", null, bundle);
                    } catch (Exception unused) {
                    }
                    if (relayAppCallback != null) {
                        int i = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
                        RelayAppCallback relayAppCallback2 = relayAppCallback;
                        if (i == 0) {
                            relayAppCallback2.onSuccess();
                        } else {
                            relayAppCallback2.onFailure(i);
                        }
                    }
                }
            });
        }
    }

    public void openRemoteDeviceMirror3(Context context, final String str, RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
            }
        } else {
            final Bundle bundle = new Bundle();
            bundle.putString("remoteDeviceId", str);
            bundle.putInt("method_version", 3);
            this.mRelayAppCallbackMap.put(str, relayAppCallback);
            final ContentResolver contentResolver = context.getContentResolver();
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.9
                @Override // java.lang.Runnable
                public void run() {
                    Bundle bundleDoCall = null;
                    try {
                        bundleDoCall = CallMethod.doCall(contentResolver, "openRemoteDeviceMirror", null, bundle);
                    } catch (Exception unused) {
                    }
                    RelayAppCallback relayAppCallback2 = (RelayAppCallback) MiuiSynergySdk.this.mRelayAppCallbackMap.get(str);
                    if (relayAppCallback2 != null) {
                        int i = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
                        if (i == 0) {
                            relayAppCallback2.onSuccess();
                        } else {
                            relayAppCallback2.onFailure(i);
                        }
                        MiuiSynergySdk.this.mRelayAppCallbackMap.remove(str);
                    }
                }
            });
        }
    }

    public void openRemoteDeviceMirrorByBtMac(Context context, final String str, int i, RelayAppCallback relayAppCallback, boolean z) {
        final Bundle bundle = new Bundle();
        bundle.putString("btMac", str);
        bundle.putInt("deviceType", i);
        bundle.putBoolean("remoteSupportLyra", z);
        this.mRelayAppCallbackMap.put(str, relayAppCallback);
        final ContentResolver contentResolver = context.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.7
            @Override // java.lang.Runnable
            public void run() {
                Bundle bundleDoCall = null;
                try {
                    bundleDoCall = CallMethod.doCall(contentResolver, "openRemoteDeviceMirrorByBtMac", null, bundle);
                } catch (Exception unused) {
                }
                RelayAppCallback relayAppCallback2 = (RelayAppCallback) MiuiSynergySdk.this.mRelayAppCallbackMap.get(str);
                if (relayAppCallback2 != null) {
                    int i2 = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
                    if (i2 == 0) {
                        relayAppCallback2.onSuccess();
                    } else {
                        relayAppCallback2.onFailure(i2);
                    }
                }
            }
        });
    }

    public void pinAppOptionExposed(Context context, String str) {
        if (isMirrorNotAlive()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("appName", str);
        if (CallMethod.doCall(context.getContentResolver(), "pinAppOptionExposed", null, bundle) == null) {
            Log.e("MiuiSynergy", "pinAppOptionExposed result = null");
        }
    }

    public List queryAllRemoteDevices(Context context, boolean z) {
        if (isMirrorNotAlive()) {
            return null;
        }
        return queryRemoteDevices(context, null, z);
    }

    public List queryMirrorDesktopDevices(Context context) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "queryMirrorDeviceList", null, new Bundle());
            bundleDoCall.setClassLoader(MirrorDesktopInfo.class.getClassLoader());
            ArrayList parcelableArrayList = bundleDoCall.getParcelableArrayList("mirrorDevice");
            if (parcelableArrayList == null || parcelableArrayList.size() == 0) {
                return null;
            }
            return parcelableArrayList;
        } catch (Exception unused) {
        }
    }

    public MirrorDesktopInfo queryMirrorDesktopInfo(Context context) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "queryMirrorDesktopInfo", null, new Bundle());
            bundleDoCall.setClassLoader(MirrorDesktopInfo.class.getClassLoader());
            return (MirrorDesktopInfo) bundleDoCall.getParcelable("mirrorDesktopInfo");
        } catch (Exception unused) {
            return null;
        }
    }

    public int queryOpenOnSynergy(Context context, String str, final QueryOpenCallback queryOpenCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        final Bundle bundle = new Bundle();
        bundle.putString("extension", str);
        final ContentResolver contentResolver = context.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.1
            @Override // java.lang.Runnable
            public void run() {
                Bundle bundleDoCall = CallMethod.doCall(contentResolver, "queryOpenOnSynergy", null, bundle);
                if (bundleDoCall == null) {
                    queryOpenCallback.onQueryResult(null);
                    return;
                }
                ArrayList parcelableArrayList = bundleDoCall.getParcelableArrayList("optionList");
                if (parcelableArrayList == null) {
                    queryOpenCallback.onQueryResult(null);
                    return;
                }
                ArrayList arrayList = new ArrayList(parcelableArrayList.size());
                int size = parcelableArrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = parcelableArrayList.get(i);
                    i++;
                    Bundle bundle2 = (Bundle) obj;
                    Option option = new Option();
                    option.id = bundle2.getString("id");
                    option.title = bundle2.getString(SearchContract.SearchResultColumn.COLUMN_TITLE);
                    option.icon = (Bitmap) bundle2.getParcelable("icon");
                    arrayList.add(option);
                }
                queryOpenCallback.onQueryResult(arrayList);
            }
        });
        return 0;
    }

    public RemoteDeviceInfo queryRemoteDevice(Context context, String str) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString("remoteDeviceId", str);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "queryRemoteDevice", null, bundle);
            bundleDoCall.setClassLoader(RemoteDeviceInfo.class.getClassLoader());
            return (RemoteDeviceInfo) bundleDoCall.getParcelable("remoteDevice");
        } catch (Exception unused) {
            return null;
        }
    }

    public List queryRemoteDevices(Context context, boolean z) {
        if (isMirrorNotAlive()) {
            return null;
        }
        return queryRemoteDevices(context, RemoteDeviceInfo.MANUFACTURER_XIAOMI, z);
    }

    public SameAccountAccessPoint querySameAccountApInfo(Context context) {
        if (isMirrorNotAlive()) {
            return null;
        }
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "querySameAccountAp", null, null);
            if (bundleDoCall != null && !TextUtils.isEmpty(bundleDoCall.getString("apSsid"))) {
                return new SameAccountAccessPoint(bundleDoCall);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public int registerMediaRelayCallback(Context context, MediaRelayCallback mediaRelayCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        this.mMediaRelayCallback = mediaRelayCallback;
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mIMediaRelayCallback.asBinder());
        try {
            CallMethod.doCall(context.getContentResolver(), "registerMediaRelayCallback", null, bundle);
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int registerSameAccountApCallback(Context context, SameAccountApCallback sameAccountApCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBinder("apCallback", this.mISameAccountApCallback.asBinder());
            this.mSameAccountApCallback = sameAccountApCallback;
            CallMethod.doCall(context.getContentResolver(), "registerApCallback", null, bundle);
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int registerTakePhotoCallback(Context context, MirrorTakePhotoCallback mirrorTakePhotoCallback) {
        if (isMirrorNotAlive() || this.mTakingPhotoManager != null) {
            return -1;
        }
        TakingPhotoManager takingPhotoManager = new TakingPhotoManager();
        this.mTakingPhotoManager = takingPhotoManager;
        return takingPhotoManager.registerTakePhotoCallback(context, mirrorTakePhotoCallback);
    }

    public int releaseMirrorAdvConnection(Context context, AdvConnectionCallback advConnectionCallback) {
        AdvConnectionCallbackWrapper advConnectionCallbackWrapper;
        if (isMirrorNotAlive() || (advConnectionCallbackWrapper = this.mMirrorAdvConnectionCallback) == null || advConnectionCallbackWrapper.mCallback.get() != advConnectionCallback) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mMirrorAdvConnectionCallback.asBinder());
        try {
            CallMethod.doCall(context.getContentResolver(), "releaseMirrorAdvConnection", null, bundle);
        } catch (Exception unused) {
        }
        this.mMirrorAdvConnectionCallback = null;
        return 0;
    }

    public void removePinApp(final Context context, final String str, final String str2, final String str3, final String str4) {
        if (isMirrorNotAlive()) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MiuiSynergySdk.$r8$lambda$XiXKgUB__kaKSkPhjn4jlksrAlM(str, str2, str3, str4, context);
            }
        });
    }

    public void removePinAppChangeCallBack(PinAppChangeCallBack pinAppChangeCallBack, Context context) {
        this.mPinAppChangeCallBack = null;
        BroadcastReceiver broadcastReceiver = this.mPinAppReceiver;
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
            this.mPinAppReceiver = null;
        }
    }

    public void removeRemoteDeviceListener(Context context, IRemoteDeviceListener iRemoteDeviceListener) {
        if (AppUtil.getMirrorInstallStatus(context) == 2) {
            Log.e("MiuiSynergy", "removeRemoteDeviceListener: failed, mirror isn't installed");
            return;
        }
        CopyOnWriteArrayList copyOnWriteArrayList = this.mRemoteDeviceListeners;
        if (copyOnWriteArrayList == null) {
            return;
        }
        copyOnWriteArrayList.remove(iRemoteDeviceListener);
        if (!this.mRemoteDeviceListeners.isEmpty() || this.mReceiver == null) {
            return;
        }
        context.getApplicationContext().unregisterReceiver(this.mReceiver);
        this.mReceiver = null;
    }

    public int saveToSynergy(Activity activity, ClipData clipData, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("clipData", clipData);
        bundle.putString("extra", str);
        bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
        try {
            CallMethod.doCall(activity.getContentResolver(), "saveToSynergy", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void sendAppToRemoteDevice(Context context, String str, int i, String str2, String str3, String str4, RelayAppCallback relayAppCallback) {
        if (!isMirrorNotAlive()) {
            sendAppToRemoteDevice(context, str, i, str2, str3, str4, false, relayAppCallback);
        } else if (relayAppCallback != null) {
            relayAppCallback.onFailure(-1);
        }
    }

    public int sendFileToDeviceByBtMac(Activity activity, String str, int i, ArrayList arrayList, int i2) {
        if (i2 == 0) {
            Log.d("MiuiSynergy", "oneHopSendFileByMirror");
            if (isMirrorNotAlive()) {
                return -1;
            }
            final Bundle bundle = new Bundle();
            bundle.putString("btMac", str);
            bundle.putInt("deviceType", i);
            bundle.putParcelableArrayList("fileList", arrayList);
            final ContentResolver contentResolver = activity.getContentResolver();
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.3
                @Override // java.lang.Runnable
                public void run() {
                    CallMethod.doCall(contentResolver, "sendFileByBtMac", null, bundle);
                }
            });
        } else if (i2 == 1) {
            String packageName = activity.getApplicationContext().getPackageName();
            Log.d("MiuiSynergy", "oneHopSendFileByMiShare");
            Intent intent = new Intent(ACTION_MISHARE_ONEHOP);
            intent.setPackage(PACKAGE_MISHARE);
            intent.setFlags(1);
            Bundle bundle2 = new Bundle();
            bundle2.putString("btMac", str);
            int size = arrayList.size();
            int i3 = 0;
            while (i3 < size) {
                Object obj = arrayList.get(i3);
                i3++;
                activity.grantUriPermission(PACKAGE_MISHARE, (Uri) obj, 1);
            }
            bundle2.putParcelableArrayList("fileList", arrayList);
            bundle2.putInt("deviceType", i);
            bundle2.putString("oneHopSharePackage", packageName);
            intent.putExtras(bundle2);
            activity.startForegroundService(intent);
        }
        return 0;
    }

    public int setAgreePrivacy(Context context, boolean z) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("enable", z);
        try {
            CallMethod.doCall(context.getContentResolver(), "setAgreePrivacy", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Deprecated
    public void setAllRemoteDeviceListener(Context context, IRemoteDeviceListener iRemoteDeviceListener) {
        setRemoteDeviceListener(context, null, iRemoteDeviceListener);
    }

    public int setMediaRelayVolume(Context context, String str, int i) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            bundle.putInt("volume", i);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "setMediaRelayVolume", null, bundle);
            if (bundleDoCall == null) {
                return -1;
            }
            return bundleDoCall.getInt(a.p, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setMirrorDeviceStatusCallback(Context context, String str, final MirrorDeviceStatusCallback mirrorDeviceStatusCallback) throws MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        if (TextUtils.isEmpty(str)) {
            Log.e("MiuiSynergy", "onRemoteDeviceStatus deviceId == null!");
            return;
        }
        IMirrorDeviceStatusCallback.Stub stub = new IMirrorDeviceStatusCallback.Stub() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.12
            @Override // com.xiaomi.mirror.IMirrorDeviceStatusCallback
            public void onRemoteDeviceStatus(int i, boolean z) throws RemoteException {
                MirrorDeviceStatusCallback mirrorDeviceStatusCallback2 = mirrorDeviceStatusCallback;
                if (mirrorDeviceStatusCallback2 != null) {
                    mirrorDeviceStatusCallback2.onRemoteDeviceStatus(i, z);
                }
            }
        };
        Bundle bundle = new Bundle();
        bundle.putBinder(CallMethod.ARG_DEVICE_STATUS_CALLBACK, mirrorDeviceStatusCallback != null ? stub.asBinder() : null);
        bundle.putString(CallMethod.ARG_DEVICE_ID, str);
        if (CallMethod.doCall(context.getContentResolver(), "setMirrorDeviceStatus", null, bundle) == null) {
            throw new MiuiSynergyException("local miui+ not support");
        }
    }

    public void setPinAppChangeCallBack(PinAppChangeCallBack pinAppChangeCallBack, Context context) {
        Log.d("MiuiSynergy", "setPinAppChangeCallBack");
        this.mPinAppChangeCallBack = pinAppChangeCallBack;
        this.mPinAppReceiver = new PinAppChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MIRROR_PIN_APP_CHANGED);
        context.registerReceiver(this.mPinAppReceiver, intentFilter, 2);
    }

    @Deprecated
    public void setRemoteDeviceListener(Context context, IRemoteDeviceListener iRemoteDeviceListener) {
        setRemoteDeviceListener(context, RemoteDeviceInfo.MANUFACTURER_XIAOMI, iRemoteDeviceListener);
    }

    public void setShareCallback(Context context, String str, final MirrorShareCallback mirrorShareCallback) throws MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        IMirrorShareCallback.Stub stub = new IMirrorShareCallback.Stub() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.11
            @Override // com.xiaomi.mirror.IMirrorShareCallback
            public void onStateChanged(String str2) throws RemoteException {
                MirrorShareCallback mirrorShareCallback2 = mirrorShareCallback;
                if (mirrorShareCallback2 != null) {
                    mirrorShareCallback2.onStateChanged(str2);
                } else {
                    Log.e("MiuiSynergy", "onStateChanged callback == null!");
                }
            }
        };
        Bundle bundle = new Bundle();
        bundle.putBinder(CallMethod.ARG_SHARE_CALLBACK, stub.asBinder());
        bundle.putString(CallMethod.ARG_SHARE_CALLBACK_TAG, str);
        if (CallMethod.doCall(context.getContentResolver(), "setShareCallback", null, bundle) == null) {
            throw new MiuiSynergyException("local miui+ not support");
        }
    }

    public int startMediaRelay(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "startMediaRelay", null, bundle);
            if (bundleDoCall == null) {
                return -1;
            }
            return bundleDoCall.getInt(a.p, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void startRemoteMainMirrorDisplay(Context context, final String str, RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
            }
        } else {
            final Bundle bundle = new Bundle();
            bundle.putString("remoteDeviceId", str);
            this.mRelayAppCallbackMap.put(str, relayAppCallback);
            final ContentResolver contentResolver = context.getContentResolver();
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.8
                @Override // java.lang.Runnable
                public void run() {
                    Bundle bundleDoCall = null;
                    try {
                        bundleDoCall = CallMethod.doCall(contentResolver, "startRemoteMainMirrorDisplay", null, bundle);
                    } catch (Exception unused) {
                    }
                    RelayAppCallback relayAppCallback2 = (RelayAppCallback) MiuiSynergySdk.this.mRelayAppCallbackMap.get(str);
                    if (relayAppCallback2 != null) {
                        int i = bundleDoCall != null ? bundleDoCall.getInt(a.p, -1) : -1;
                        if (i >= 0) {
                            relayAppCallback2.onSuccess();
                        } else {
                            relayAppCallback2.onFailure(i);
                        }
                    }
                    MiuiSynergySdk.this.mRelayAppCallbackMap.remove(str);
                }
            });
        }
    }

    public boolean startShare(Context context, String str, boolean z) throws MiuiSynergyException {
        if (isMirrorNotAlive()) {
            throw new MiuiSynergyException("mirror process not alive");
        }
        Bundle bundle = new Bundle();
        bundle.putString(CallMethod.ARG_DEVICE_ID, str);
        bundle.putBoolean(CallMethod.ARG_IS_START, z);
        Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "startShare", str, bundle);
        if (bundleDoCall != null) {
            return bundleDoCall.getBoolean("enable");
        }
        throw new MiuiSynergyException("local miui+ not support");
    }

    public int stopMediaRelay(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CallMethod.ARG_DEVICE_ID, str);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "stopMediaRelay", null, bundle);
            if (bundleDoCall == null) {
                return -1;
            }
            return bundleDoCall.getInt(a.p, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void syncPinApp(final ArrayList arrayList, final Context context, final GetAllPinAppListCallBack getAllPinAppListCallBack) {
        if (!isMirrorNotAlive()) {
            this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    MiuiSynergySdk.$r8$lambda$4wyapIkE1ILSQJBfrc77vO_Mkz4(arrayList, context, getAllPinAppListCallBack);
                }
            });
        } else if (getAllPinAppListCallBack != null) {
            getAllPinAppListCallBack.onFail(-1, "mirror process not alive");
        }
    }

    public int takePhotoCancel(Activity activity) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        return takePhotoCancel(activity, "");
    }

    public int takePhotoOnSynergy(Activity activity, ChooseFileCallback chooseFileCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        return takePhotoOnSynergy(activity, "", chooseFileCallback);
    }

    public int unRegisterSameAccountApCallback(Context context) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        try {
            CallMethod.doCall(context.getContentResolver(), "unRegisterApCallback", null, null);
            this.mSameAccountApCallback = null;
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int unregisterMediaRelayCallback(Context context, MediaRelayCallback mediaRelayCallback) {
        if (isMirrorNotAlive() || this.mMediaRelayCallback != mediaRelayCallback) {
            return -1;
        }
        this.mMediaRelayCallback = null;
        Bundle bundle = new Bundle();
        bundle.putBinder("callback", this.mIMediaRelayCallback.asBinder());
        try {
            CallMethod.doCall(context.getContentResolver(), "unregisterMediaRelayCallback", null, bundle);
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int unregisterTakePhotoCallback(Context context, MirrorTakePhotoCallback mirrorTakePhotoCallback) {
        TakingPhotoManager takingPhotoManager;
        if (isMirrorNotAlive() || (takingPhotoManager = this.mTakingPhotoManager) == null) {
            return -1;
        }
        return takingPhotoManager.unregisterTakePhotoCallback(context, mirrorTakePhotoCallback);
    }

    public int updateTitle(Context context, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putString(SearchContract.SearchResultColumn.COLUMN_TITLE, str);
        try {
            CallMethod.doCall(context.getContentResolver(), "updateTitle", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setRemoteDeviceListener(Context context, String str, IRemoteDeviceListener iRemoteDeviceListener) {
        if (iRemoteDeviceListener != null) {
            addRemoteDeviceListener(context, iRemoteDeviceListener);
        } else {
            removeRemoteDeviceListener(context, this.mRemoteDeviceListener);
        }
        this.mRemoteDeviceListener = iRemoteDeviceListener;
    }

    private List queryRemoteDevices(Context context, String str, boolean z) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("remoteHistory", z);
            bundle.putString("remoteDeviceManufacturer", str);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "queryRemoteDevices", null, bundle);
            bundleDoCall.setClassLoader(RemoteDeviceInfo.class.getClassLoader());
            ArrayList parcelableArrayList = bundleDoCall.getParcelableArrayList("remoteDevices");
            if (parcelableArrayList == null || parcelableArrayList.size() == 0) {
                return null;
            }
            return parcelableArrayList;
        } catch (Exception unused) {
        }
    }

    public boolean isSupportSendApp(Context context, String str, String str2) {
        if (isMirrorNotAlive()) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString(f.a.e, str);
        bundle.putString("deviceType", str2);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSupportSendApp", null, bundle);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSupportTakePhoto(Context context, boolean z) {
        if (isMirrorNotAlive()) {
            return false;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("includeLyra", z);
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "isSupportTakePhoto", null, bundle);
            return bundleDoCall != null && bundleDoCall.getBoolean("enable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int takePhotoCancel(Activity activity, String str) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("id", str);
        }
        try {
            CallMethod.doCall(activity.getContentResolver(), "takePhotoCancel", null, bundle);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int takePhotoOnSynergy(Activity activity, String str, final ChooseFileCallback chooseFileCallback) {
        if (isMirrorNotAlive()) {
            return -1;
        }
        final Bundle bundle = new Bundle();
        bundle.putInt("displayId", activity.getWindow().getDecorView().getDisplay().getDisplayId());
        bundle.putString("id", str);
        final ContentResolver contentResolver = activity.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.4
            @Override // java.lang.Runnable
            public void run() {
                Bundle bundleDoCall = CallMethod.doCall(contentResolver, "takePhotoFromSynergy", null, bundle);
                chooseFileCallback.onFileChosen(bundleDoCall != null ? (ClipData) bundleDoCall.getParcelable("clipData") : null);
            }
        });
        return 0;
    }

    public void sendAppToRemoteDevice(Context context, String str, int i, String str2, String str3, String str4, boolean z, final RelayAppCallback relayAppCallback) {
        if (isMirrorNotAlive()) {
            if (relayAppCallback != null) {
                relayAppCallback.onFailure(-1);
                return;
            }
            return;
        }
        final Bundle bundle = new Bundle();
        bundle.putString("remoteDeviceId", str);
        bundle.putInt("taskId", i);
        bundle.putString(f.a.e, str2);
        bundle.putString("appName", str3);
        bundle.putString("ref", str4);
        bundle.putBoolean("onlyCast", z);
        final ContentResolver contentResolver = context.getContentResolver();
        this.mExecutor.execute(new Runnable() { // from class: com.xiaomi.mirror.synergy.MiuiSynergySdk.6
            @Override // java.lang.Runnable
            public void run() {
                Bundle bundleDoCall = CallMethod.doCall(contentResolver, "sendAppToRemoteDevice", null, bundle);
                RelayAppCallback relayAppCallback2 = relayAppCallback;
                if (relayAppCallback2 != null) {
                    if (bundleDoCall == null) {
                        relayAppCallback2.onFailure(0);
                        return;
                    }
                    int i2 = bundleDoCall.getInt("sendAppResult", 0);
                    RelayAppCallback relayAppCallback3 = relayAppCallback;
                    if (i2 == 1) {
                        relayAppCallback3.onSuccess();
                    } else {
                        relayAppCallback3.onFailure(i2);
                    }
                }
            }
        });
    }
}
