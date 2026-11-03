package com.android.phone.settings.appfunctions;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateItemConfig {
    private final boolean enabled;
    private final int englishName;
    private final String hintText;
    private final int localizedName;
    private final String settingKey;
    private final String settingScreenKey;

    public DeviceStateItemConfig(boolean z, String str, String str2, String str3, int i, int i2) {
        this.enabled = z;
        this.settingKey = str;
        this.settingScreenKey = str2;
        this.hintText = str3;
        this.englishName = i;
        this.localizedName = i2;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getSettingKey() {
        return this.settingKey;
    }

    public String getSettingScreenKey() {
        return this.settingScreenKey;
    }

    public String getHintText() {
        return this.hintText;
    }

    public int getEnglishName() {
        return this.englishName;
    }

    public int getLocalizedName() {
        return this.localizedName;
    }
}
