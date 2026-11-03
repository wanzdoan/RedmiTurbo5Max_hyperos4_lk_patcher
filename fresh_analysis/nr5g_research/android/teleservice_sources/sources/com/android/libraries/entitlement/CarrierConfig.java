package com.android.libraries.entitlement;

import android.net.Network;
import com.android.libraries.entitlement.utils.UrlConnectionFactory;

/* JADX INFO: loaded from: classes.dex */
public abstract class CarrierConfig {

    public abstract class Builder {
        public abstract CarrierConfig build();

        public abstract Builder setClientTs43(String str);

        public abstract Builder setEapAkaRealm(String str);

        public abstract Builder setPreflattenCookies(boolean z);

        public abstract Builder setServerUrl(String str);

        public abstract Builder setTimeoutInSec(int i);

        public abstract Builder setUseHttpPost(boolean z);
    }

    public abstract String clientTs43();

    public abstract String eapAkaRealm();

    public abstract Network network();

    public abstract boolean preflattenCookies();

    public abstract String serverUrl();

    public abstract int timeoutInSec();

    public abstract UrlConnectionFactory urlConnectionFactory();

    public abstract boolean useHttpPost();

    public static Builder builder() {
        return new AutoValue_CarrierConfig.Builder().setServerUrl("").setClientTs43("").setUseHttpPost(false).setTimeoutInSec(30).setEapAkaRealm("nai.epc").setPreflattenCookies(false);
    }
}
