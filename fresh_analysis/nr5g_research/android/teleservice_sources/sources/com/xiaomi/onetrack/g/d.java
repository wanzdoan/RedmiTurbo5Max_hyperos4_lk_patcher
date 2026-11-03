package com.xiaomi.onetrack.g;

import android.text.TextUtils;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.a.a;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class d {
    public int d;
    public JSONArray e;
    public String f;
    public String g;

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (this.d == 200) {
                this.e = jSONObject.optJSONArray("messageIds");
                return;
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(ResponseObject.DEFAULT_MESSAGE_FAILED);
            this.f = jSONObjectOptJSONObject.optString(a.m);
            this.g = jSONObjectOptJSONObject.optString(a.J);
        } catch (Exception unused) {
        }
    }

    public String toString() {
        return "PublishResponse{code=" + this.d + ", messageIds=" + this.e + ", message='" + this.f + "', status='" + this.g + "'}";
    }
}
