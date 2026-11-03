package com.aios.apptoolsdk;

import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppParam {
    private final String description;
    private final List enumValues;
    private final String name;
    private final boolean required;
    private final String type;

    private AppParam(String str, String str2, String str3, boolean z, List list) {
        this.name = str;
        this.type = str2;
        this.description = str3;
        this.required = z;
        this.enumValues = list;
    }

    public static AppParam string(String str, String str2) {
        return new AppParam(str, KeyStringSettingItem.TYPE, str2, false, null);
    }

    public static AppParam string(String str, String str2, boolean z) {
        return new AppParam(str, KeyStringSettingItem.TYPE, str2, z, null);
    }

    public static AppParam number(String str, String str2) {
        return new AppParam(str, "number", str2, false, null);
    }

    public static AppParam number(String str, String str2, boolean z) {
        return new AppParam(str, "number", str2, z, null);
    }

    public static AppParam integer(String str, String str2) {
        return new AppParam(str, "integer", str2, false, null);
    }

    public static AppParam integer(String str, String str2, boolean z) {
        return new AppParam(str, "integer", str2, z, null);
    }

    public static AppParam bool(String str, String str2) {
        return new AppParam(str, "boolean", str2, false, null);
    }

    public static AppParam bool(String str, String str2, boolean z) {
        return new AppParam(str, "boolean", str2, z, null);
    }
}
