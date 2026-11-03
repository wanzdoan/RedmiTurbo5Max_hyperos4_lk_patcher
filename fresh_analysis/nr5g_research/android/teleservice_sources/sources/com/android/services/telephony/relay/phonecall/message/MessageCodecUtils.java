package com.android.services.telephony.relay.phonecall.message;

import android.net.Uri;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;
import com.android.services.telephony.relay.phonecall.message.request.RequestObject;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;

/* JADX INFO: loaded from: classes.dex */
public class MessageCodecUtils {
    public static final String RELAY_SCHEME = "relay";
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    private static final String TAG = "MessageCodecUtils";

    interface DecodeCallback {
        void onDecode(String str, String str2, String str3);
    }

    public static String encodeRequestMessage(RequestObject requestObject) {
        String str = "relay://" + requestObject.getMethodName() + ":" + requestObject.getMethodId() + "/request?" + requestObject.getParamJson();
        RelayLog.logi(TAG, "encodeRequestMessage:" + RelayUtils.Relay.hideSensitiveInfo(str));
        return str;
    }

    public static void decodeRequestMessage(String str, DecodeCallback decodeCallback) {
        Uri uri = Uri.parse(str);
        if (!RELAY_SCHEME.equals(uri.getScheme())) {
            RelayLog.logi(TAG, "decodeRequestMessage not relay message!");
            decodeCallback.onDecode(null, null, null);
            return;
        }
        String host = uri.getHost();
        int port = uri.getPort();
        String query = uri.getQuery();
        RelayLog.logi(TAG, "decodeRequestMessage:" + RelayUtils.Relay.hideSensitiveInfo(str));
        decodeCallback.onDecode(host, String.valueOf(port), query);
    }

    public static String encodeResponseMessage(String str, String str2, ResponseObject responseObject) {
        String str3 = "relay://" + str + ":" + str2 + "/response?" + responseObject.toJson();
        RelayLog.logi(TAG, "encodeResponseMessage message:" + RelayUtils.Relay.hideSensitiveInfo(str3));
        return str3;
    }

    public static void decodeResponseMessage(String str, DecodeCallback decodeCallback) {
        Uri uri = Uri.parse(str);
        if (!RELAY_SCHEME.equals(uri.getScheme())) {
            RelayLog.logi(TAG, "decodeResponseMessage not relay message!");
            decodeCallback.onDecode(null, null, null);
            return;
        }
        decodeCallback.onDecode(uri.getHost(), String.valueOf(uri.getPort()), uri.getQuery());
        RelayLog.logi(TAG, "decodeResponseMessage message:" + RelayUtils.Relay.hideSensitiveInfo(str));
    }

    public static RequestObject getRequestObject(RelayApi relayApi, String str) {
        RequestObject requestObject = new RequestObject();
        requestObject.setRelayApi(relayApi);
        requestObject.setParamJson(str);
        return requestObject;
    }
}
