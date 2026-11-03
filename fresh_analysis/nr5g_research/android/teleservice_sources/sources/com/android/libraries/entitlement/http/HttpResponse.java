package com.android.libraries.entitlement.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class HttpResponse {
    public abstract String body();

    public abstract int contentType();

    public abstract List cookies();

    public abstract String location();

    public abstract int responseCode();

    public abstract String responseMessage();

    public abstract class Builder {
        abstract HttpResponse autoBuild();

        abstract List cookies();

        public abstract Builder setBody(String str);

        public abstract Builder setContentType(int i);

        public abstract Builder setCookies(List list);

        public abstract Builder setLocation(String str);

        public abstract Builder setResponseCode(int i);

        public abstract Builder setResponseMessage(String str);

        public HttpResponse build() {
            setCookies(Collections.unmodifiableList(new ArrayList(cookies())));
            return autoBuild();
        }
    }

    public static Builder builder() {
        return new AutoValue_HttpResponse.Builder().setContentType(-1).setBody("").setResponseCode(0).setResponseMessage("").setCookies(Collections.EMPTY_LIST).setLocation("");
    }

    public String toShortDebugString() {
        return "HttpResponse{contentType=" + contentType() + " body=(" + body().length() + " characters) responseCode=" + responseCode() + " responseMessage=" + responseMessage() + " cookies=[" + cookies().size() + " cookies] location=" + location();
    }
}
