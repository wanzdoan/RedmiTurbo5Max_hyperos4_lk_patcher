package com.android.phone.settings.search.provider;

/* JADX INFO: loaded from: classes.dex */
public class RawData {
    String extras;
    int iconResId;
    String intentAction;
    String intentTargetClass;
    String intentTargetPackage;
    String intentUri;
    String summaryOn;
    String title;

    public RawData(String str, String str2, String str3, String str4) {
        this(str, str2, str3, str4, null);
    }

    public RawData(String str, String str2, String str3, String str4, String str5) {
        this.title = str;
        this.intentAction = str2;
        this.intentTargetPackage = str3;
        this.intentTargetClass = str4;
        this.extras = str5;
    }

    public void setPath(String str) {
        this.summaryOn = str;
    }

    public void setExtras(String str) {
        this.extras = str;
    }

    public void setIntentUri(String str) {
        this.intentUri = str;
    }

    public void setIconResId(int i) {
        this.iconResId = i;
    }
}
