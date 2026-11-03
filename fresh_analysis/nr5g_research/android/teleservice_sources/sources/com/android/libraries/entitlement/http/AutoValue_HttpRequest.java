package com.android.libraries.entitlement.http;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import android.net.Network;
import com.android.libraries.entitlement.utils.UrlConnectionFactory;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class AutoValue_HttpRequest extends HttpRequest {
    private final Network network;
    private final JSONObject postData;
    private final String requestMethod;
    private final Map requestProperties;
    private final int timeoutInSec;
    private final String url;

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public UrlConnectionFactory urlConnectionFactory() {
        return null;
    }

    private AutoValue_HttpRequest(String str, String str2, JSONObject jSONObject, Map map, int i, Network network, UrlConnectionFactory urlConnectionFactory) {
        this.url = str;
        this.requestMethod = str2;
        this.postData = jSONObject;
        this.requestProperties = map;
        this.timeoutInSec = i;
        this.network = network;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public String url() {
        return this.url;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public String requestMethod() {
        return this.requestMethod;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public JSONObject postData() {
        return this.postData;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public Map requestProperties() {
        return this.requestProperties;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public int timeoutInSec() {
        return this.timeoutInSec;
    }

    @Override // com.android.libraries.entitlement.http.HttpRequest
    public Network network() {
        return this.network;
    }

    public String toString() {
        return "HttpRequest{url=" + this.url + ", requestMethod=" + this.requestMethod + ", postData=" + this.postData + ", requestProperties=" + this.requestProperties + ", timeoutInSec=" + this.timeoutInSec + ", network=" + this.network + ", urlConnectionFactory=null}";
    }

    public boolean equals(Object obj) {
        Network network;
        if (obj == this) {
            return true;
        }
        if (obj instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) obj;
            if (this.url.equals(httpRequest.url()) && this.requestMethod.equals(httpRequest.requestMethod()) && this.postData.equals(httpRequest.postData()) && this.requestProperties.equals(httpRequest.requestProperties()) && this.timeoutInSec == httpRequest.timeoutInSec() && ((network = this.network) != null ? network.equals(httpRequest.network()) : httpRequest.network() == null)) {
                httpRequest.urlConnectionFactory();
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = (((((((((this.url.hashCode() ^ 1000003) * 1000003) ^ this.requestMethod.hashCode()) * 1000003) ^ this.postData.hashCode()) * 1000003) ^ this.requestProperties.hashCode()) * 1000003) ^ this.timeoutInSec) * 1000003;
        Network network = this.network;
        return ((network == null ? 0 : network.hashCode()) ^ iHashCode) * 1000003;
    }

    final class Builder extends HttpRequest.Builder {
        private Network network;
        private JSONObject postData;
        private String requestMethod;
        private Map requestProperties;
        private byte set$0;
        private int timeoutInSec;
        private String url;

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setUrlConnectionFactory(UrlConnectionFactory urlConnectionFactory) {
            return this;
        }

        Builder() {
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setUrl(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null url");
                return null;
            }
            this.url = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setRequestMethod(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null requestMethod");
                return null;
            }
            this.requestMethod = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setPostData(JSONObject jSONObject) {
            if (jSONObject == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null postData");
                return null;
            }
            this.postData = jSONObject;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        HttpRequest.Builder setRequestProperties(Map map) {
            if (map == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null requestProperties");
                return null;
            }
            this.requestProperties = map;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setTimeoutInSec(int i) {
            this.timeoutInSec = i;
            this.set$0 = (byte) (this.set$0 | 1);
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        public HttpRequest.Builder setNetwork(Network network) {
            this.network = network;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpRequest.Builder
        HttpRequest autoBuild() {
            String str;
            String str2;
            JSONObject jSONObject;
            Map map;
            if (this.set$0 != 1 || (str = this.url) == null || (str2 = this.requestMethod) == null || (jSONObject = this.postData) == null || (map = this.requestProperties) == null) {
                StringBuilder sb = new StringBuilder();
                if (this.url == null) {
                    sb.append(" url");
                }
                if (this.requestMethod == null) {
                    sb.append(" requestMethod");
                }
                if (this.postData == null) {
                    sb.append(" postData");
                }
                if (this.requestProperties == null) {
                    sb.append(" requestProperties");
                }
                if ((this.set$0 & 1) == 0) {
                    sb.append(" timeoutInSec");
                }
                OrtUtil$$ExternalSyntheticBUOutline2.m("Missing required properties:", sb);
                return null;
            }
            return new AutoValue_HttpRequest(str, str2, jSONObject, map, this.timeoutInSec, this.network, null);
        }
    }
}
