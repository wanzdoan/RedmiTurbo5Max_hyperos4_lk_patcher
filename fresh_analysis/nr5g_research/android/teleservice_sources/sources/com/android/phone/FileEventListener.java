package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public abstract class FileEventListener {
    String path;

    public abstract void onEvent(int i, String str);

    public FileEventListener(String str) {
        this.path = str;
    }
}
