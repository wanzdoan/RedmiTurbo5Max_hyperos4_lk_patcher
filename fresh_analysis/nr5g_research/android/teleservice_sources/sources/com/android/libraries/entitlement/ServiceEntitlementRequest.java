package com.android.libraries.entitlement;

import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public abstract class ServiceEntitlementRequest {

    public abstract class Builder {
        public abstract ServiceEntitlementRequest build();

        public abstract Builder setAcceptContentType(String str);

        public abstract Builder setAppName(String str);

        public abstract Builder setAppVersion(String str);

        public abstract Builder setAuthenticationToken(String str);

        public abstract Builder setBoostType(String str);

        public abstract Builder setConfigurationVersion(int i);

        public abstract Builder setEntitlementVersion(String str);

        public abstract Builder setGid1(String str);

        public abstract Builder setNotificationAction(int i);

        public abstract Builder setNotificationToken(String str);

        public abstract Builder setTemporaryToken(String str);

        public abstract Builder setTerminalId(String str);

        public abstract Builder setTerminalModel(String str);

        public abstract Builder setTerminalSoftwareVersion(String str);

        public abstract Builder setTerminalVendor(String str);
    }

    public abstract String acceptContentType();

    public abstract String appName();

    public abstract String appVersion();

    public abstract String authenticationToken();

    public abstract String boostType();

    public abstract int configurationVersion();

    public abstract String entitlementVersion();

    public abstract String gid1();

    public abstract int notificationAction();

    public abstract String notificationToken();

    public abstract String temporaryToken();

    public abstract String terminalId();

    public abstract String terminalModel();

    public abstract String terminalSoftwareVersion();

    public abstract String terminalVendor();

    public static Builder builder() {
        return new AutoValue_ServiceEntitlementRequest.Builder().setConfigurationVersion(0).setEntitlementVersion("2.0").setAuthenticationToken("").setTemporaryToken("").setTerminalId("").setTerminalVendor(Build.MANUFACTURER).setTerminalModel(Build.MODEL).setTerminalSoftwareVersion(Build.VERSION.RELEASE).setAppName("").setAppVersion("").setNotificationToken("").setNotificationAction(2).setAcceptContentType("application/json, text/vnd.wap.connectivity-xml").setBoostType("").setGid1("");
    }
}
