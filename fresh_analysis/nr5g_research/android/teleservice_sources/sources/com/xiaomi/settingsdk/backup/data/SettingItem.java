package com.xiaomi.settingsdk.backup.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SettingItem implements Parcelable, Comparable {
    protected static final String KEY_VALUE = "value";
    protected static final String TAG = "SettingsBackup";
    public String key;

    protected abstract Object getJsonValue();

    protected abstract String getType();

    protected abstract void setValueFromJson(JSONObject jSONObject);

    protected abstract Object stringToValue(String str);

    protected abstract String valueToString(Object obj);

    public SettingItem() {
        throw new RuntimeException("Stub!");
    }

    public Object getValue() {
        throw new RuntimeException("Stub!");
    }

    public void setValue(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public static SettingItem fromJson(JSONObject jSONObject) {
        throw new RuntimeException("Stub!");
    }

    public JSONObject toJson() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    protected void fillFromParcel(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Comparable
    public int compareTo(SettingItem settingItem) {
        throw new RuntimeException("Stub!");
    }
}
