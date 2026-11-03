package com.xiaomi.mirror;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class RemoteDeviceInfo implements Parcelable {
    public static final int ACCOUNT_STATUS_NOT_VERIFIED = -1;
    public static final int ACCOUNT_STATUS_VERIFIED_FAILED = 2;
    public static final int ACCOUNT_STATUS_VERIFIED_SUCCEED = 1;
    public static final int ACCOUNT_STATUS_VERIFIED_UNKNOWN = 0;
    public static final int CONNECT_TYPE_ADVANCED = 2;
    public static final int CONNECT_TYPE_ADVANCED_USER_AWARE = 6;
    public static final int CONNECT_TYPE_BASIC = 1;
    public static final int CONNECT_TYPE_NONE = 0;
    public static final int CONNECT_TYPE_USER_AWARE = 4;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.xiaomi.mirror.RemoteDeviceInfo.1
        @Override // android.os.Parcelable.Creator
        public RemoteDeviceInfo createFromParcel(Parcel parcel) {
            return new RemoteDeviceInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public RemoteDeviceInfo[] newArray(int i) {
            return new RemoteDeviceInfo[i];
        }
    };
    public static final int ENABLE_MIRROR_NOT_SUPPORT_LOCAL = -1;
    public static final int ENABLE_MIRROR_NOT_SUPPORT_REMOTE = -2;
    public static final int ENABLE_MIRROR_SUPPORT = 1;
    static final String KEY_ACCOUNT_STATUS = "account_status";
    static final String KEY_ADDRESS = "address";
    static final String KEY_APP_VERSION = "app_version";
    static final String KEY_BT_MAC = "bt_mac";
    static final String KEY_CAPABILITIES = "capabilities";
    static final String KEY_CONNECT_TYPE = "connect_type";
    static final String KEY_DESKTOP_SWITCH = "desktop_switch";
    static final String KEY_DEVICE_ID = "device_id";
    static final String KEY_DISPLAY_NAME = "display_name";
    static final String KEY_HANDOFF_SWITCH = "handoff_switch";
    static final String KEY_ID = "id";
    static final String KEY_IS_FLIP_STATE = "is_flip_state";
    static final String KEY_IS_LYRA = "is_lyra";
    static final String KEY_IS_MEDIA_RELAY = "is_media_relay";
    static final String KEY_IS_MIRROR_ENABLED = "is_mirror_enabled";
    static final String KEY_IS_SHOW_MIRROR = "is_show_mirror";
    static final String KEY_IS_SINK = "is_sink";
    static final String KEY_IS_SUBSCREEN_ENABLED = "is_subscreen_enabled";
    public static final String KEY_IS_SUPPORT_ENABLE_MIRROR = "is_support_enable_mirror";
    static final String KEY_IS_SUPPORT_SEND_APP = "is_support_send_app";
    static final String KEY_IS_SUPPORT_SUB_SCREEN = "is_support_subscreen";
    static final String KEY_LAST_CONNECT_TIMESTAMP = "last_connect_timestamp";
    static final String KEY_MANUFACTURER = "manufacturer";
    static final String KEY_PLATFORM = "platform";
    static final String KEY_PRODUCT_TYPE = "product_type";
    static final String KEY_SN = "sn";
    public static final String MANUFACTURER_OTHER = "other";
    public static final String MANUFACTURER_XIAOMI = "xiaomi";
    public static final int MEDIA_RELAY_DISABLED = 0;
    public static final int MEDIA_RELAY_ENABLED = 1;
    public static final int MEDIA_RELAY_NOT_SUPPORT = -1;
    public static final int MIRROR_DISABLED = -1;
    public static final int MIRROR_DISABLED_BY_USER = -7;
    public static final int MIRROR_DISABLED_LOCAL_TEMPERATURE_HIGH = -2;
    public static final int MIRROR_ENABLED = 1;
    public static final int MIRROR_ENABLED_BY_USER = 7;
    public static final int MIRROR_ENABLED_LOCAL_TEMPERATURE_LOW = 2;
    public static final String PLATFORM_ANDROID_PAD = "AndroidPad";
    public static final String PLATFORM_ANDROID_PAD_CAR = "AndroidPadCar";
    public static final String PLATFORM_ANDROID_PHONE = "AndroidPhone";
    public static final String PLATFORM_WINDOWS = "Windows";
    public static final int SUBSCREEN_DISABLED = -1;
    public static final int SUBSCREEN_DISABLED_LOCAL_TEMPERATURE_HIGH = -2;
    public static final int SUBSCREEN_ENABLED = 1;
    public static final int SUBSCREEN_ENABLED_LOCAL_TEMPERATURE_LOW = 2;
    private Bundle mBundle;

    public RemoteDeviceInfo() {
        Bundle bundle = new Bundle();
        this.mBundle = bundle;
        bundle.putString("platform", PLATFORM_ANDROID_PHONE);
        this.mBundle.putInt(KEY_CONNECT_TYPE, 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getAccountStatus() {
        return this.mBundle.getInt(KEY_ACCOUNT_STATUS);
    }

    public String getAddress() {
        return this.mBundle.getString(KEY_ADDRESS);
    }

    public long getAppVersion() {
        return this.mBundle.getLong(KEY_APP_VERSION);
    }

    public String getBtMac() {
        return this.mBundle.getString(KEY_BT_MAC);
    }

    public Bundle getBundle() {
        return new Bundle(this.mBundle);
    }

    public HashMap getCapabilities() {
        return (HashMap) this.mBundle.getSerializable(KEY_CAPABILITIES);
    }

    public int getConnectType() {
        return this.mBundle.getInt(KEY_CONNECT_TYPE);
    }

    public boolean getDesktopSwitch() {
        return this.mBundle.getBoolean(KEY_DESKTOP_SWITCH);
    }

    public String getDeviceId() {
        return this.mBundle.getString(KEY_DEVICE_ID);
    }

    public String getDisplayName() {
        return this.mBundle.getString(KEY_DISPLAY_NAME);
    }

    public boolean getHandOffSwitch() {
        return this.mBundle.getBoolean(KEY_HANDOFF_SWITCH);
    }

    public String getId() {
        return this.mBundle.getString(KEY_ID);
    }

    public long getLastConnectTimestamp() {
        return this.mBundle.getLong(KEY_LAST_CONNECT_TIMESTAMP);
    }

    public String getManufacturer() {
        return this.mBundle.getString(KEY_MANUFACTURER);
    }

    public Bundle getMutableBundle() {
        return this.mBundle;
    }

    public String getPlatform() {
        return this.mBundle.getString("platform");
    }

    public String getProductType() {
        return this.mBundle.getString(KEY_PRODUCT_TYPE);
    }

    public String getSn() {
        return this.mBundle.getString(KEY_SN);
    }

    public boolean isFlipState() {
        return this.mBundle.getBoolean(KEY_IS_FLIP_STATE);
    }

    public boolean isLyra() {
        return this.mBundle.getBoolean(KEY_IS_LYRA, false);
    }

    public int isMediaRelay() {
        return this.mBundle.getInt(KEY_IS_MEDIA_RELAY, -1);
    }

    public int isMirrorEnabled() {
        return this.mBundle.getInt(KEY_IS_MIRROR_ENABLED, 1);
    }

    public boolean isShowMirror() {
        return this.mBundle.getBoolean(KEY_IS_SHOW_MIRROR);
    }

    public Boolean isSink() {
        if (this.mBundle.containsKey(KEY_IS_SINK)) {
            return Boolean.valueOf(this.mBundle.getBoolean(KEY_IS_SINK));
        }
        return null;
    }

    public int isSubscreenEnabled() {
        return this.mBundle.getInt(KEY_IS_SUBSCREEN_ENABLED, 1);
    }

    public int isSupportEnableMirror() {
        return this.mBundle.getInt(KEY_IS_SUPPORT_ENABLE_MIRROR, -1);
    }

    public boolean isSupportSendApp() {
        return this.mBundle.getBoolean(KEY_IS_SUPPORT_SEND_APP);
    }

    public boolean isSupportSubScreen() {
        return this.mBundle.getBoolean(KEY_IS_SUPPORT_SUB_SCREEN, true);
    }

    public String toString() {
        return "RemoteDeviceInfo{mBundle=" + this.mBundle + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mBundle);
    }

    public RemoteDeviceInfo(Parcel parcel) {
        this.mBundle = new Bundle();
        this.mBundle = parcel.readBundle(getClass().getClassLoader());
    }
}
