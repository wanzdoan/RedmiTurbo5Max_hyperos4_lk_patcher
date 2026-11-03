package com.android.libraries.entitlement;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
final class AutoValue_ServiceEntitlementRequest extends ServiceEntitlementRequest {
    private final String acceptContentType;
    private final String appName;
    private final String appVersion;
    private final String authenticationToken;
    private final String boostType;
    private final int configurationVersion;
    private final String entitlementVersion;
    private final String gid1;
    private final int notificationAction;
    private final String notificationToken;
    private final String temporaryToken;
    private final String terminalId;
    private final String terminalModel;
    private final String terminalSoftwareVersion;
    private final String terminalVendor;

    private AutoValue_ServiceEntitlementRequest(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i2, String str11, String str12, String str13) {
        this.configurationVersion = i;
        this.entitlementVersion = str;
        this.authenticationToken = str2;
        this.temporaryToken = str3;
        this.terminalId = str4;
        this.terminalVendor = str5;
        this.terminalModel = str6;
        this.terminalSoftwareVersion = str7;
        this.appName = str8;
        this.appVersion = str9;
        this.notificationToken = str10;
        this.notificationAction = i2;
        this.acceptContentType = str11;
        this.boostType = str12;
        this.gid1 = str13;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public int configurationVersion() {
        return this.configurationVersion;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String entitlementVersion() {
        return this.entitlementVersion;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String authenticationToken() {
        return this.authenticationToken;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String temporaryToken() {
        return this.temporaryToken;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String terminalId() {
        return this.terminalId;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String terminalVendor() {
        return this.terminalVendor;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String terminalModel() {
        return this.terminalModel;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String terminalSoftwareVersion() {
        return this.terminalSoftwareVersion;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String appName() {
        return this.appName;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String appVersion() {
        return this.appVersion;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String notificationToken() {
        return this.notificationToken;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public int notificationAction() {
        return this.notificationAction;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String acceptContentType() {
        return this.acceptContentType;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String boostType() {
        return this.boostType;
    }

    @Override // com.android.libraries.entitlement.ServiceEntitlementRequest
    public String gid1() {
        return this.gid1;
    }

    public String toString() {
        return "ServiceEntitlementRequest{configurationVersion=" + this.configurationVersion + ", entitlementVersion=" + this.entitlementVersion + ", authenticationToken=" + this.authenticationToken + ", temporaryToken=" + this.temporaryToken + ", terminalId=" + this.terminalId + ", terminalVendor=" + this.terminalVendor + ", terminalModel=" + this.terminalModel + ", terminalSoftwareVersion=" + this.terminalSoftwareVersion + ", appName=" + this.appName + ", appVersion=" + this.appVersion + ", notificationToken=" + this.notificationToken + ", notificationAction=" + this.notificationAction + ", acceptContentType=" + this.acceptContentType + ", boostType=" + this.boostType + ", gid1=" + this.gid1 + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ServiceEntitlementRequest) {
            ServiceEntitlementRequest serviceEntitlementRequest = (ServiceEntitlementRequest) obj;
            if (this.configurationVersion == serviceEntitlementRequest.configurationVersion() && this.entitlementVersion.equals(serviceEntitlementRequest.entitlementVersion()) && this.authenticationToken.equals(serviceEntitlementRequest.authenticationToken()) && this.temporaryToken.equals(serviceEntitlementRequest.temporaryToken()) && this.terminalId.equals(serviceEntitlementRequest.terminalId()) && this.terminalVendor.equals(serviceEntitlementRequest.terminalVendor()) && this.terminalModel.equals(serviceEntitlementRequest.terminalModel()) && this.terminalSoftwareVersion.equals(serviceEntitlementRequest.terminalSoftwareVersion()) && this.appName.equals(serviceEntitlementRequest.appName()) && this.appVersion.equals(serviceEntitlementRequest.appVersion()) && this.notificationToken.equals(serviceEntitlementRequest.notificationToken()) && this.notificationAction == serviceEntitlementRequest.notificationAction() && this.acceptContentType.equals(serviceEntitlementRequest.acceptContentType()) && this.boostType.equals(serviceEntitlementRequest.boostType()) && this.gid1.equals(serviceEntitlementRequest.gid1())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.gid1.hashCode() ^ ((((((((((((((((((((((((((((this.configurationVersion ^ 1000003) * 1000003) ^ this.entitlementVersion.hashCode()) * 1000003) ^ this.authenticationToken.hashCode()) * 1000003) ^ this.temporaryToken.hashCode()) * 1000003) ^ this.terminalId.hashCode()) * 1000003) ^ this.terminalVendor.hashCode()) * 1000003) ^ this.terminalModel.hashCode()) * 1000003) ^ this.terminalSoftwareVersion.hashCode()) * 1000003) ^ this.appName.hashCode()) * 1000003) ^ this.appVersion.hashCode()) * 1000003) ^ this.notificationToken.hashCode()) * 1000003) ^ this.notificationAction) * 1000003) ^ this.acceptContentType.hashCode()) * 1000003) ^ this.boostType.hashCode()) * 1000003);
    }

    final class Builder extends ServiceEntitlementRequest.Builder {
        private String acceptContentType;
        private String appName;
        private String appVersion;
        private String authenticationToken;
        private String boostType;
        private int configurationVersion;
        private String entitlementVersion;
        private String gid1;
        private int notificationAction;
        private String notificationToken;
        private byte set$0;
        private String temporaryToken;
        private String terminalId;
        private String terminalModel;
        private String terminalSoftwareVersion;
        private String terminalVendor;

        Builder() {
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setConfigurationVersion(int i) {
            this.configurationVersion = i;
            this.set$0 = (byte) (this.set$0 | 1);
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setEntitlementVersion(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null entitlementVersion");
                return null;
            }
            this.entitlementVersion = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setAuthenticationToken(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null authenticationToken");
                return null;
            }
            this.authenticationToken = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setTemporaryToken(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null temporaryToken");
                return null;
            }
            this.temporaryToken = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setTerminalId(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null terminalId");
                return null;
            }
            this.terminalId = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setTerminalVendor(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null terminalVendor");
                return null;
            }
            this.terminalVendor = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setTerminalModel(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null terminalModel");
                return null;
            }
            this.terminalModel = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setTerminalSoftwareVersion(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null terminalSoftwareVersion");
                return null;
            }
            this.terminalSoftwareVersion = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setAppName(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null appName");
                return null;
            }
            this.appName = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setAppVersion(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null appVersion");
                return null;
            }
            this.appVersion = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setNotificationToken(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null notificationToken");
                return null;
            }
            this.notificationToken = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setNotificationAction(int i) {
            this.notificationAction = i;
            this.set$0 = (byte) (this.set$0 | 2);
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setAcceptContentType(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null acceptContentType");
                return null;
            }
            this.acceptContentType = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setBoostType(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null boostType");
                return null;
            }
            this.boostType = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest.Builder setGid1(String str) {
            if (str == null) {
                GenericObject$$ExternalSyntheticBUOutline0.m("Null gid1");
                return null;
            }
            this.gid1 = str;
            return this;
        }

        @Override // com.android.libraries.entitlement.ServiceEntitlementRequest.Builder
        public ServiceEntitlementRequest build() {
            String str;
            String str2;
            String str3;
            String str4;
            String str5;
            String str6;
            String str7;
            String str8;
            String str9;
            String str10;
            String str11;
            String str12;
            String str13;
            if (this.set$0 != 3 || (str = this.entitlementVersion) == null || (str2 = this.authenticationToken) == null || (str3 = this.temporaryToken) == null || (str4 = this.terminalId) == null || (str5 = this.terminalVendor) == null || (str6 = this.terminalModel) == null || (str7 = this.terminalSoftwareVersion) == null || (str8 = this.appName) == null || (str9 = this.appVersion) == null || (str10 = this.notificationToken) == null || (str11 = this.acceptContentType) == null || (str12 = this.boostType) == null || (str13 = this.gid1) == null) {
                StringBuilder sb = new StringBuilder();
                if ((this.set$0 & 1) == 0) {
                    sb.append(" configurationVersion");
                }
                if (this.entitlementVersion == null) {
                    sb.append(" entitlementVersion");
                }
                if (this.authenticationToken == null) {
                    sb.append(" authenticationToken");
                }
                if (this.temporaryToken == null) {
                    sb.append(" temporaryToken");
                }
                if (this.terminalId == null) {
                    sb.append(" terminalId");
                }
                if (this.terminalVendor == null) {
                    sb.append(" terminalVendor");
                }
                if (this.terminalModel == null) {
                    sb.append(" terminalModel");
                }
                if (this.terminalSoftwareVersion == null) {
                    sb.append(" terminalSoftwareVersion");
                }
                if (this.appName == null) {
                    sb.append(" appName");
                }
                if (this.appVersion == null) {
                    sb.append(" appVersion");
                }
                if (this.notificationToken == null) {
                    sb.append(" notificationToken");
                }
                if ((this.set$0 & 2) == 0) {
                    sb.append(" notificationAction");
                }
                if (this.acceptContentType == null) {
                    sb.append(" acceptContentType");
                }
                if (this.boostType == null) {
                    sb.append(" boostType");
                }
                if (this.gid1 == null) {
                    sb.append(" gid1");
                }
                OrtUtil$$ExternalSyntheticBUOutline2.m("Missing required properties:", sb);
                return null;
            }
            return new AutoValue_ServiceEntitlementRequest(this.configurationVersion, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, this.notificationAction, str11, str12, str13);
        }
    }
}
