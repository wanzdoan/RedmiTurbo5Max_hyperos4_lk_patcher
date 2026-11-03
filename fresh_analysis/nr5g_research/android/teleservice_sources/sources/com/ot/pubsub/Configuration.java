package com.ot.pubsub;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class Configuration {
    private String a;
    private String b;
    private String c;
    private String d;
    private boolean e;
    private String f;
    private boolean g;
    private String h;
    private boolean i;
    private boolean j;
    private boolean k;

    private Configuration(Builder builder) {
        this.i = false;
        this.j = false;
        this.k = false;
        this.a = builder.a;
        this.d = builder.b;
        this.b = builder.c;
        this.c = builder.d;
        this.e = builder.e;
        this.f = builder.f;
        this.j = builder.g;
        this.k = builder.h;
        this.h = builder.i;
        this.i = builder.j;
    }

    public String getAppId() {
        return this.a;
    }

    public String getChannel() {
        return this.d;
    }

    public String getProjectId() {
        return this.b;
    }

    public String getPrivateKeyId() {
        return this.c;
    }

    public boolean isInternational() {
        return this.e;
    }

    public String getRegion() {
        return this.f;
    }

    public boolean isOverrideMiuiRegionSetting() {
        return this.j;
    }

    public boolean isNeedGzipAndEncrypt() {
        return this.k;
    }

    public String getInstanceId() {
        return this.h;
    }

    public boolean isUseCustomPrivacyPolicy() {
        return this.i;
    }

    public class Builder {
        private String a;
        private String b;
        private String c;
        private String d;
        private boolean e;
        private String f;
        private String i;
        private boolean g = false;
        private boolean h = false;
        private boolean j = false;

        public Builder setAppId(String str) {
            this.a = str;
            return this;
        }

        public Builder setChannel(String str) {
            this.b = str;
            return this;
        }

        public Builder setProjectId(String str) {
            this.c = str;
            return this;
        }

        public Builder setPrivateKeyId(String str) {
            this.d = str;
            return this;
        }

        public Builder setInternational(boolean z) {
            this.e = z;
            return this;
        }

        public Builder setRegion(String str) {
            this.f = str;
            return this;
        }

        public Builder setInstanceId(String str) {
            this.i = str;
            return this;
        }

        public Builder setUseCustomPrivacyPolicy(boolean z) {
            this.j = z;
            return this;
        }

        public Builder setOverrideMiuiRegionSetting(boolean z) {
            this.g = z;
            return this;
        }

        public Builder setNeedGzipAndEncrypt(boolean z) {
            this.h = z;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

    public String toString() {
        try {
            return "Configuration{appId='" + a(this.a) + "', channel='" + this.d + "'mProjectId='" + a(this.b) + "', mPrivateKeyId='" + a(this.c) + "', mInternational=" + this.e + ", mNeedGzipAndEncrypt=" + this.k + ", mRegion='" + this.f + "', overrideMiuiRegionSetting=" + this.j + ", instanceId=" + a(this.h) + '}';
        } catch (Exception unused) {
            return "";
        }
    }

    private String a(String str) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str) && str.length() > 4) {
            for (int i = 0; i < str.length(); i++) {
                if (i == 0 || i == 1 || i == str.length() - 2 || i == str.length() - 1) {
                    sb.append(str.charAt(i));
                } else {
                    sb.append("*");
                }
            }
        } else {
            sb.append(str);
        }
        return sb.toString();
    }
}
