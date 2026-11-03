package com.android.libraries.entitlement.http;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class AutoValue_HttpResponse extends HttpResponse {
    private final String body;
    private final int contentType;
    private final List cookies;
    private final String location;
    private final int responseCode;
    private final String responseMessage;

    private AutoValue_HttpResponse(int i, String str, int i2, String str2, List list, String str3) {
        this.contentType = i;
        this.body = str;
        this.responseCode = i2;
        this.responseMessage = str2;
        this.cookies = list;
        this.location = str3;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public int contentType() {
        return this.contentType;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public String body() {
        return this.body;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public int responseCode() {
        return this.responseCode;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public String responseMessage() {
        return this.responseMessage;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public List cookies() {
        return this.cookies;
    }

    @Override // com.android.libraries.entitlement.http.HttpResponse
    public String location() {
        return this.location;
    }

    public String toString() {
        return "HttpResponse{contentType=" + this.contentType + ", body=" + this.body + ", responseCode=" + this.responseCode + ", responseMessage=" + this.responseMessage + ", cookies=" + this.cookies + ", location=" + this.location + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) obj;
            if (this.contentType == httpResponse.contentType() && this.body.equals(httpResponse.body()) && this.responseCode == httpResponse.responseCode() && this.responseMessage.equals(httpResponse.responseMessage()) && this.cookies.equals(httpResponse.cookies()) && this.location.equals(httpResponse.location())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.location.hashCode() ^ ((((((((((this.contentType ^ 1000003) * 1000003) ^ this.body.hashCode()) * 1000003) ^ this.responseCode) * 1000003) ^ this.responseMessage.hashCode()) * 1000003) ^ this.cookies.hashCode()) * 1000003);
    }

    final class Builder extends HttpResponse.Builder {
        private String body;
        private int contentType;
        private List cookies;
        private String location;
        private int responseCode;
        private String responseMessage;
        private byte set$0;

        Builder() {
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setContentType(int i) {
            this.contentType = i;
            this.set$0 = (byte) (this.set$0 | 1);
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setBody(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null body");
                return null;
            }
            this.body = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setResponseCode(int i) {
            this.responseCode = i;
            this.set$0 = (byte) (this.set$0 | 2);
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setResponseMessage(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null responseMessage");
                return null;
            }
            this.responseMessage = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setCookies(List list) {
            if (list == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null cookies");
                return null;
            }
            this.cookies = list;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        List cookies() {
            List list = this.cookies;
            if (list != null) {
                return list;
            }
            OrtUtil$$ExternalSyntheticBUOutline0.m("Property \"cookies\" has not been set");
            return null;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        public HttpResponse.Builder setLocation(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null location");
                return null;
            }
            this.location = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.http.HttpResponse.Builder
        HttpResponse autoBuild() {
            String str;
            String str2;
            List list;
            String str3;
            if (this.set$0 != 3 || (str = this.body) == null || (str2 = this.responseMessage) == null || (list = this.cookies) == null || (str3 = this.location) == null) {
                StringBuilder sb = new StringBuilder();
                if ((this.set$0 & 1) == 0) {
                    sb.append(" contentType");
                }
                if (this.body == null) {
                    sb.append(" body");
                }
                if ((this.set$0 & 2) == 0) {
                    sb.append(" responseCode");
                }
                if (this.responseMessage == null) {
                    sb.append(" responseMessage");
                }
                if (this.cookies == null) {
                    sb.append(" cookies");
                }
                if (this.location == null) {
                    sb.append(" location");
                }
                OrtUtil$$ExternalSyntheticBUOutline2.m("Missing required properties:", sb);
                return null;
            }
            return new AutoValue_HttpResponse(this.contentType, str, this.responseCode, str2, list, str3);
        }
    }
}
