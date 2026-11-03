package com.xiaomi.mirror.synergy;

import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.ot.pubsub.a.a;
import com.ot.pubsub.g.f;
import com.xiaomi.mirror.util.VersionUtil;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public class RelayIconHelper implements IBinder.DeathRecipient {
    private static final String TAG = "Mirror_:RelayIconHelper";
    private IBinder mAliveBinder;
    private ContentObserver mDisableRelayIconObserver;
    private SoftReference mRefCallback;
    private ContentObserver mRelayResultObserver;
    private ContentObserver mUpdateObserver;
    private ContentObserver mVisibleObserver;
    private static final Uri URI_VISIBLE = MiuiSynergySdk.getUriFor("mirror_relay_icon_visible");
    private static final Uri URI_UPDATE = MiuiSynergySdk.getUriFor("mirror_relay_icon_update");
    private static final Uri URI_RELAY_RESULT = MiuiSynergySdk.getUriFor("mirror_relay_result");
    private static final Uri URI_DISABLE_RELAY_ICON = MiuiSynergySdk.getUriFor("mirror_disable_relay_icon");
    private static final Map deviceSupportPinMap = new HashMap();
    private volatile boolean mIsRegister = false;
    private final List mRelayIconList = new ArrayList();

    final class DisableRelayIconData {
        private List mDeviceIdList;
        private boolean mDisableRelayIcon;

        public DisableRelayIconData(boolean z, ArrayList arrayList) {
            ArrayList arrayList2 = new ArrayList();
            this.mDeviceIdList = arrayList2;
            this.mDisableRelayIcon = z;
            if (arrayList != null) {
                arrayList2.addAll(arrayList);
            }
        }

        public List getDeviceIdList() {
            return this.mDeviceIdList;
        }

        public boolean isDisableRelayIcon() {
            return this.mDisableRelayIcon;
        }

        public void setDeviceIdList(List list) {
            this.mDeviceIdList = list;
        }

        public void setDisableRelayIcon(boolean z) {
            this.mDisableRelayIcon = z;
        }
    }

    public class RelayIcon {
        private Bitmap bitmap;
        private String description;
        private boolean isSupportHandoff;
        private boolean isSupportPin;
        private String packageName;
        private String remoteDeviceId;
        private String remoteDeviceName;
        private int remoteDeviceType;

        public RelayIcon(String str, Bitmap bitmap, String str2, String str3, int i, String str4, boolean z, boolean z2) {
            this.packageName = str;
            this.bitmap = bitmap;
            this.description = str2;
            this.remoteDeviceName = str3;
            this.remoteDeviceType = i;
            this.remoteDeviceId = str4;
            this.isSupportHandoff = z;
            this.isSupportPin = z2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                RelayIcon relayIcon = (RelayIcon) obj;
                if (this.isSupportHandoff == relayIcon.isSupportHandoff && TextUtils.equals(this.packageName, relayIcon.packageName) && TextUtils.equals(this.remoteDeviceId, relayIcon.remoteDeviceId)) {
                    return true;
                }
            }
            return false;
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public String getDescription() {
            return this.description;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public String getRemoteDeviceId() {
            return this.remoteDeviceId;
        }

        public String getRemoteDeviceName() {
            return this.remoteDeviceName;
        }

        public int getRemoteDeviceType() {
            return this.remoteDeviceType;
        }

        public int hashCode() {
            if (this.packageName == null) {
                this.packageName = "";
            }
            if (this.remoteDeviceId == null) {
                this.remoteDeviceId = "";
            }
            return Objects.hash(this.packageName, this.remoteDeviceId, Boolean.valueOf(this.isSupportHandoff));
        }

        public boolean isSupportHandoff() {
            return this.isSupportHandoff;
        }

        public String toString() {
            return "RelayIcon{packageName='" + this.packageName + "', bitmap=" + this.bitmap + ", description='" + this.description + "', remoteDeviceName='" + this.remoteDeviceName + "', remoteDeviceType=" + this.remoteDeviceType + ", remoteDeviceId='" + this.remoteDeviceId + "', isSupportHandoff=" + this.isSupportHandoff + ", isSupportPin=" + this.isSupportPin + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRemoteProcess(Context context) {
        if (this.mAliveBinder != null) {
            return;
        }
        IBinder aliveBinder = MiuiSynergySdk.getInstance().getAliveBinder(context);
        this.mAliveBinder = aliveBinder;
        if (aliveBinder != null) {
            try {
                aliveBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RelayIcon getRelayIcon(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getUpdateIcon", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            String string = bundleDoCall.getString(a.p);
            Bitmap bitmap = (Bitmap) bundleDoCall.getParcelable("icon");
            if (bitmap == null) {
                return null;
            }
            return new RelayIcon(string, bitmap, bundleDoCall.getString("description"), bundleDoCall.getString("remote_device_name"), bundleDoCall.getInt("remote_device_type"), bundleDoCall.getString("remote_device_id"), bundleDoCall.getBoolean("remote_support_handoff", false), false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List getRelayIconList(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        bundle.putParcelable("uri", uri);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getUpdateIconList", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            Bundle bundle2 = bundleDoCall.getBundle("result_get_update_icon_list_first");
            Bundle bundle3 = bundleDoCall.getBundle("result_get_update_icon_list_second");
            if (bundle2 != null) {
                arrayList.add(new RelayIcon(bundle2.getString(a.p), (Bitmap) bundle2.getParcelable("icon"), bundle2.getString("description"), bundle2.getString("remote_device_name"), bundle2.getInt("remote_device_type"), bundle2.getString("remote_device_id"), bundle2.getBoolean("remote_support_handoff", false), bundle2.getBoolean("remote_support_pin", false)));
            }
            if (bundle3 != null) {
                arrayList.add(new RelayIcon(bundle3.getString(a.p), (Bitmap) bundle3.getParcelable("icon"), bundle3.getString("description"), bundle3.getString("remote_device_name"), bundle3.getInt("remote_device_type"), bundle3.getString("remote_device_id"), bundle3.getBoolean("remote_support_handoff", false), bundle3.getBoolean("remote_support_pin", false)));
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RelayResult getRelayResult(Context context, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getRelayResult", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            return bundleDoCall.getBoolean(a.p, false) ? new RelayResult(bundleDoCall.getInt("relay_type")) : new RelayResult(bundleDoCall.getInt("relay_error_code"), bundleDoCall.getString("relay_error_message"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isSupportPinByDevice(String str) {
        return Boolean.TRUE.equals(deviceSupportPinMap.get(str));
    }

    private void notifyUpdateIcon(Context context) {
        if (MiuiSynergySdk.isMirrorNotAlive()) {
            return;
        }
        try {
            CallMethod.doCall(context.getContentResolver(), "notifyUpdateIcon", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performRelayIconClick(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("extra", str);
        try {
            CallMethod.doCall(context.getContentResolver(), "performRelayIconClick", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        SoftReference softReference = this.mRefCallback;
        if (softReference != null && softReference.get() != null) {
            this.mRelayIconList.clear();
            ((RelayIconCallback) this.mRefCallback.get()).onIconHide();
        }
        IBinder iBinder = this.mAliveBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
            this.mAliveBinder = null;
        }
    }

    public DisableRelayIconData getDisableRelayIconData(Context context) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", URI_DISABLE_RELAY_ICON);
        try {
            Bundle bundleDoCall = CallMethod.doCall(context.getContentResolver(), "getDisableRelayIconData", null, bundle);
            if (bundleDoCall == null) {
                return null;
            }
            return new DisableRelayIconData(bundleDoCall.getBoolean(a.p, false), bundleDoCall.getStringArrayList("remoteDeviceIdList"));
        } catch (Exception e) {
            Log.w(TAG, "getDisableRelayIconData: " + e.getMessage());
            return null;
        }
    }

    public void performIconClick(Context context, String str) {
        performRelayIconClick(context, str);
    }

    public ComponentName performOpenPhoneApp(Context context, String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(f.a.e, str);
        bundle.putString("remoteDeviceId", str2);
        try {
            return (ComponentName) CallMethod.doCall(context.getContentResolver(), "performPinIconClick", null, bundle).getParcelable("result_activity_component");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registerCallback(final Context context, RelayIconCallback relayIconCallback, Handler handler) {
        if (relayIconCallback == null) {
            this.mRefCallback = null;
            return;
        }
        this.mRefCallback = new SoftReference(relayIconCallback);
        this.mIsRegister = true;
        this.mVisibleObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.RelayIconHelper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if ((RelayIconHelper.this.mIsRegister & (RelayIconHelper.this.mRefCallback != null)) && RelayIconHelper.this.mRefCallback.get() != null) {
                    if (MiuiSynergySdk.getInstance().getInt(context, RelayIconHelper.URI_VISIBLE, 0) != 0) {
                        Log.d(RelayIconHelper.TAG, "onIconShow");
                        ((RelayIconCallback) RelayIconHelper.this.mRefCallback.get()).onIconShow();
                        if (RelayIconHelper.this.mRelayIconList.size() > 0) {
                            for (RelayIcon relayIcon : RelayIconHelper.this.mRelayIconList) {
                                RelayIconHelper.deviceSupportPinMap.put(relayIcon.remoteDeviceId, Boolean.valueOf(relayIcon.isSupportPin));
                                Log.d(RelayIconHelper.TAG, relayIcon.toString());
                            }
                            Log.d(RelayIconHelper.TAG, "notify update on show");
                            ((RelayIconCallback) RelayIconHelper.this.mRefCallback.get()).onIconListUpdate(RelayIconHelper.this.mRelayIconList);
                        }
                    } else {
                        RelayIconHelper.this.mRelayIconList.clear();
                        Log.d(RelayIconHelper.TAG, "onIconHide");
                        ((RelayIconCallback) RelayIconHelper.this.mRefCallback.get()).onIconHide();
                    }
                }
                RelayIconHelper.this.checkRemoteProcess(context);
            }
        };
        context.getContentResolver().registerContentObserver(URI_VISIBLE, false, this.mVisibleObserver);
        this.mUpdateObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.RelayIconHelper.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                List relayIconList;
                if (!RelayIconHelper.this.mIsRegister || RelayIconHelper.this.mRefCallback == null || RelayIconHelper.this.mRefCallback.get() == null) {
                    return;
                }
                boolean zMirrorSupportOS2Version = VersionUtil.mirrorSupportOS2Version(context);
                Log.i(RelayIconHelper.TAG, "registerCallback: supportOS2Feature " + zMirrorSupportOS2Version);
                if (zMirrorSupportOS2Version) {
                    relayIconList = RelayIconHelper.this.getRelayIconList(context, RelayIconHelper.URI_UPDATE);
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(RelayIconHelper.this.getRelayIcon(context, RelayIconHelper.URI_UPDATE));
                    relayIconList = arrayList;
                }
                if (RelayIconHelper.this.mRelayIconList.isEmpty() && (relayIconList == null || relayIconList.isEmpty())) {
                    return;
                }
                boolean z2 = true;
                if (relayIconList != null && !relayIconList.isEmpty() && relayIconList.size() == RelayIconHelper.this.mRelayIconList.size()) {
                    int size = RelayIconHelper.this.mRelayIconList.size();
                    boolean z3 = false;
                    for (int i = 0; i < size; i++) {
                        RelayIcon relayIcon = (RelayIcon) RelayIconHelper.this.mRelayIconList.get(i);
                        RelayIcon relayIcon2 = (RelayIcon) relayIconList.get(i);
                        if (relayIcon != null && !relayIcon.equals(relayIcon2)) {
                            z3 = true;
                        }
                    }
                    z2 = z3;
                }
                if (!z2) {
                    Log.d(RelayIconHelper.TAG, "data not change");
                    return;
                }
                RelayIconHelper.this.mRelayIconList.clear();
                if (relayIconList != null && !relayIconList.isEmpty()) {
                    RelayIconHelper.this.mRelayIconList.addAll(relayIconList);
                }
                if (RelayIconHelper.this.mRelayIconList.size() > 0) {
                    for (RelayIcon relayIcon3 : RelayIconHelper.this.mRelayIconList) {
                        RelayIconHelper.deviceSupportPinMap.put(relayIcon3.remoteDeviceId, Boolean.valueOf(relayIcon3.isSupportPin));
                        Log.d(RelayIconHelper.TAG, relayIcon3.toString());
                    }
                }
                Log.d(RelayIconHelper.TAG, "notify update");
                ((RelayIconCallback) RelayIconHelper.this.mRefCallback.get()).onIconListUpdate(RelayIconHelper.this.mRelayIconList);
            }
        };
        context.getContentResolver().registerContentObserver(URI_UPDATE, false, this.mUpdateObserver);
        this.mRelayResultObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.RelayIconHelper.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                RelayResult relayResult;
                if (!RelayIconHelper.this.mIsRegister || RelayIconHelper.this.mRefCallback == null || RelayIconHelper.this.mRefCallback.get() == null || (relayResult = RelayIconHelper.this.getRelayResult(context, RelayIconHelper.URI_RELAY_RESULT)) == null) {
                    return;
                }
                boolean zIsRelaySuccess = relayResult.isRelaySuccess();
                RelayIconHelper relayIconHelper = RelayIconHelper.this;
                if (zIsRelaySuccess) {
                    ((RelayIconCallback) relayIconHelper.mRefCallback.get()).onRelayComplete(relayResult.getRelayType());
                } else {
                    ((RelayIconCallback) relayIconHelper.mRefCallback.get()).onRelayError(relayResult.getRelayErrorCode(), relayResult.getRelayErrMsg());
                }
            }
        };
        context.getContentResolver().registerContentObserver(URI_RELAY_RESULT, false, this.mRelayResultObserver);
        this.mDisableRelayIconObserver = new ContentObserver(handler) { // from class: com.xiaomi.mirror.synergy.RelayIconHelper.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                RelayIconCallback relayIconCallback2;
                super.onChange(z);
                if (!RelayIconHelper.this.mIsRegister || RelayIconHelper.this.mRefCallback == null || (relayIconCallback2 = (RelayIconCallback) RelayIconHelper.this.mRefCallback.get()) == null) {
                    return;
                }
                DisableRelayIconData disableRelayIconData = RelayIconHelper.this.getDisableRelayIconData(context);
                if (disableRelayIconData == null) {
                    relayIconCallback2.disableRelayIcon(false, null);
                } else {
                    relayIconCallback2.disableRelayIcon(disableRelayIconData.isDisableRelayIcon(), disableRelayIconData.getDeviceIdList());
                }
            }
        };
        context.getContentResolver().registerContentObserver(URI_DISABLE_RELAY_ICON, false, this.mDisableRelayIconObserver);
        checkRemoteProcess(context);
        notifyUpdateIcon(context);
    }

    public void unRegisterCallback(Context context) {
        this.mIsRegister = false;
        if (this.mVisibleObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mVisibleObserver);
            this.mVisibleObserver = null;
        }
        if (this.mUpdateObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mUpdateObserver);
            this.mUpdateObserver = null;
        }
        if (this.mRelayResultObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mRelayResultObserver);
            this.mRelayResultObserver = null;
        }
        if (this.mDisableRelayIconObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mDisableRelayIconObserver);
            this.mDisableRelayIconObserver = null;
        }
        IBinder iBinder = this.mAliveBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
            this.mAliveBinder = null;
        }
    }

    final class RelayResult {
        private String mRelayErrMsg;
        private int mRelayErrorCode;
        private boolean mRelaySuccess = true;
        private int mRelayType;

        public RelayResult(int i) {
            this.mRelayType = i;
        }

        public String getRelayErrMsg() {
            return this.mRelayErrMsg;
        }

        public int getRelayErrorCode() {
            return this.mRelayErrorCode;
        }

        public int getRelayType() {
            return this.mRelayType;
        }

        public boolean isRelaySuccess() {
            return this.mRelaySuccess;
        }

        public RelayResult(int i, String str) {
            this.mRelayErrorCode = i;
            this.mRelayErrMsg = str;
        }
    }
}
