package com.android.libraries.entitlement;

/* JADX INFO: loaded from: classes.dex */
public class ServiceEntitlementException extends Exception {
    private final int mError;
    private final int mHttpStatus;
    private final String mRetryAfter;

    public ServiceEntitlementException(int i, String str) {
        this(i, 0, "", str);
    }

    public ServiceEntitlementException(int i, int i2, String str, String str2) {
        super(str2);
        this.mError = i;
        this.mHttpStatus = i2;
        this.mRetryAfter = str;
    }

    public ServiceEntitlementException(int i, String str, Throwable th) {
        this(i, 0, "", str, th);
    }

    public ServiceEntitlementException(int i, int i2, String str, String str2, Throwable th) {
        super(str2, th);
        this.mError = i;
        this.mHttpStatus = i2;
        this.mRetryAfter = str;
    }

    public int getErrorCode() {
        return this.mError;
    }

    public int getHttpStatus() {
        return this.mHttpStatus;
    }

    public String getRetryAfter() {
        return this.mRetryAfter;
    }
}
