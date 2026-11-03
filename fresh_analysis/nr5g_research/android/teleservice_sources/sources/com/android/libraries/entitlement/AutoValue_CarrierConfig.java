package com.android.libraries.entitlement;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import android.net.Network;
import com.android.libraries.entitlement.utils.UrlConnectionFactory;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
final class AutoValue_CarrierConfig extends CarrierConfig {
    private final String clientTs43;
    private final String eapAkaRealm;
    private final Network network;
    private final boolean preflattenCookies;
    private final String serverUrl;
    private final int timeoutInSec;
    private final boolean useHttpPost;

    @Override // com.android.libraries.entitlement.CarrierConfig
    public UrlConnectionFactory urlConnectionFactory() {
        return null;
    }

    private AutoValue_CarrierConfig(String str, String str2, boolean z, int i, Network network, UrlConnectionFactory urlConnectionFactory, String str3, boolean z2) {
        this.serverUrl = str;
        this.clientTs43 = str2;
        this.useHttpPost = z;
        this.timeoutInSec = i;
        this.network = network;
        this.eapAkaRealm = str3;
        this.preflattenCookies = z2;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public String serverUrl() {
        return this.serverUrl;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public String clientTs43() {
        return this.clientTs43;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public boolean useHttpPost() {
        return this.useHttpPost;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public int timeoutInSec() {
        return this.timeoutInSec;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public Network network() {
        return this.network;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public String eapAkaRealm() {
        return this.eapAkaRealm;
    }

    @Override // com.android.libraries.entitlement.CarrierConfig
    public boolean preflattenCookies() {
        return this.preflattenCookies;
    }

    public String toString() {
        return "CarrierConfig{serverUrl=" + this.serverUrl + ", clientTs43=" + this.clientTs43 + ", useHttpPost=" + this.useHttpPost + ", timeoutInSec=" + this.timeoutInSec + ", network=" + this.network + ", urlConnectionFactory=null, eapAkaRealm=" + this.eapAkaRealm + ", preflattenCookies=" + this.preflattenCookies + "}";
    }

    public boolean equals(Object obj) {
        Network network;
        if (obj == this) {
            return true;
        }
        if (obj instanceof CarrierConfig) {
            CarrierConfig carrierConfig = (CarrierConfig) obj;
            if (this.serverUrl.equals(carrierConfig.serverUrl()) && this.clientTs43.equals(carrierConfig.clientTs43()) && this.useHttpPost == carrierConfig.useHttpPost() && this.timeoutInSec == carrierConfig.timeoutInSec() && ((network = this.network) != null ? network.equals(carrierConfig.network()) : carrierConfig.network() == null)) {
                carrierConfig.urlConnectionFactory();
                if (this.eapAkaRealm.equals(carrierConfig.eapAkaRealm()) && this.preflattenCookies == carrierConfig.preflattenCookies()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = (((((((this.serverUrl.hashCode() ^ 1000003) * 1000003) ^ this.clientTs43.hashCode()) * 1000003) ^ (this.useHttpPost ? 1231 : 1237)) * 1000003) ^ this.timeoutInSec) * 1000003;
        Network network = this.network;
        return ((((iHashCode ^ (network == null ? 0 : network.hashCode())) * (-721379959)) ^ this.eapAkaRealm.hashCode()) * 1000003) ^ (this.preflattenCookies ? 1231 : 1237);
    }

    final class Builder extends CarrierConfig.Builder {
        private String clientTs43;
        private String eapAkaRealm;
        private Network network;
        private boolean preflattenCookies;
        private String serverUrl;
        private byte set$0;
        private int timeoutInSec;
        private boolean useHttpPost;

        Builder() {
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setServerUrl(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null serverUrl");
                return null;
            }
            this.serverUrl = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setClientTs43(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null clientTs43");
                return null;
            }
            this.clientTs43 = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setUseHttpPost(boolean z) {
            this.useHttpPost = z;
            this.set$0 = (byte) (this.set$0 | 1);
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setTimeoutInSec(int i) {
            this.timeoutInSec = i;
            this.set$0 = (byte) (this.set$0 | 2);
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setEapAkaRealm(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null eapAkaRealm");
                return null;
            }
            this.eapAkaRealm = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig.Builder setPreflattenCookies(boolean z) {
            this.preflattenCookies = z;
            this.set$0 = (byte) (this.set$0 | 4);
            return this;
        }

        @Override // com.android.libraries.entitlement.CarrierConfig.Builder
        public CarrierConfig build() {
            String str;
            String str2;
            String str3;
            if (this.set$0 != 7 || (str = this.serverUrl) == null || (str2 = this.clientTs43) == null || (str3 = this.eapAkaRealm) == null) {
                StringBuilder sb = new StringBuilder();
                if (this.serverUrl == null) {
                    sb.append(" serverUrl");
                }
                if (this.clientTs43 == null) {
                    sb.append(" clientTs43");
                }
                if ((this.set$0 & 1) == 0) {
                    sb.append(" useHttpPost");
                }
                if ((this.set$0 & 2) == 0) {
                    sb.append(" timeoutInSec");
                }
                if (this.eapAkaRealm == null) {
                    sb.append(" eapAkaRealm");
                }
                if ((this.set$0 & 4) == 0) {
                    sb.append(" preflattenCookies");
                }
                OrtUtil$$ExternalSyntheticBUOutline2.m("Missing required properties:", sb);
                return null;
            }
            return new AutoValue_CarrierConfig(str, str2, this.useHttpPost, this.timeoutInSec, this.network, null, str3, this.preflattenCookies);
        }
    }
}
