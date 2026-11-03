package com.android.phone.spradar;

import android.os.Parcelable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpradarFuncInfo implements Parcelable {
    public abstract SpradarFuncInfo fromJson(int i, JSONObject jSONObject);

    public abstract int getFuncId();

    public abstract int getFuncType();

    public abstract int getResponseType();
}
