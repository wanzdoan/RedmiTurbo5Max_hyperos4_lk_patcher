package com.android.libraries.entitlement.http;

import android.content.res.Resources;
import android.net.Network;
import com.android.libraries.entitlement.utils.UrlConnectionFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class HttpRequest {
    public abstract Network network();

    public abstract JSONObject postData();

    public abstract String requestMethod();

    public abstract Map requestProperties();

    public abstract int timeoutInSec();

    public abstract String url();

    public abstract UrlConnectionFactory urlConnectionFactory();

    public abstract class Builder {
        private final Map mRequestProperties = new HashMap();

        abstract HttpRequest autoBuild();

        public abstract Builder setNetwork(Network network);

        public abstract Builder setPostData(JSONObject jSONObject);

        public abstract Builder setRequestMethod(String str);

        abstract Builder setRequestProperties(Map map);

        public abstract Builder setTimeoutInSec(int i);

        public abstract Builder setUrl(String str);

        public abstract Builder setUrlConnectionFactory(UrlConnectionFactory urlConnectionFactory);

        public HttpRequest build() {
            HashMap map = new HashMap();
            for (Map.Entry entry : this.mRequestProperties.entrySet()) {
                map.put((String) entry.getKey(), Collections.unmodifiableList(new ArrayList((Collection) entry.getValue())));
            }
            setRequestProperties(Collections.unmodifiableMap(map));
            return autoBuild();
        }

        public Builder addRequestProperty(String str, String str2) {
            if (!this.mRequestProperties.containsKey(str)) {
                this.mRequestProperties.put(str, new ArrayList());
            }
            ((List) this.mRequestProperties.get(str)).add(str2);
            return this;
        }

        public Builder addRequestProperty(String str, List list) {
            if (!this.mRequestProperties.containsKey(str)) {
                this.mRequestProperties.put(str, new ArrayList());
            }
            ((List) this.mRequestProperties.get(str)).addAll(list);
            return this;
        }
    }

    public static Builder builder() {
        return new AutoValue_HttpRequest.Builder().setUrl("").setRequestMethod("").setPostData(new JSONObject()).setTimeoutInSec(30).addRequestProperty("Accept-Language", Resources.getSystem().getConfiguration().getLocales().get(0).toLanguageTag());
    }
}
