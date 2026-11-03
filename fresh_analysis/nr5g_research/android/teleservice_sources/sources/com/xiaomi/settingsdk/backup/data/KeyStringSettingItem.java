package com.xiaomi.settingsdk.backup.data;

import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class KeyStringSettingItem extends SettingItem {
    public static final Parcelable.Creator CREATOR = null;
    public static final String TYPE = "string";

    public KeyStringSettingItem() {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaomi.settingsdk.backup.data.SettingItem
    public String stringToValue(String str) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaomi.settingsdk.backup.data.SettingItem
    public String valueToString(String str) {
        throw new RuntimeException("Stub!");
    }

    @Override // com.xiaomi.settingsdk.backup.data.SettingItem
    protected Object getJsonValue() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.xiaomi.settingsdk.backup.data.SettingItem
    protected void setValueFromJson(JSONObject jSONObject) {
        throw new RuntimeException("Stub!");
    }

    @Override // com.xiaomi.settingsdk.backup.data.SettingItem
    protected String getType() {
        throw new RuntimeException("Stub!");
    }
}
