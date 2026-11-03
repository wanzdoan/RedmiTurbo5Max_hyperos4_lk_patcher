package com.ot.pubsub.a;

import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubTrack;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static final String a = "B";
    public static final String b = "H";
    private static final String c = "EventDataBuilder";

    public static String a(String str, JSONObject jSONObject, Configuration configuration, PubSubTrack.IEventHook iEventHook, JSONObject jSONObject2, com.ot.pubsub.util.q qVar) throws JSONException {
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put(b, com.ot.pubsub.h.a.a(str, configuration, iEventHook, qVar));
        jSONObject3.put(a, com.ot.pubsub.util.m.a(jSONObject, jSONObject2));
        return jSONObject3.toString();
    }
}
