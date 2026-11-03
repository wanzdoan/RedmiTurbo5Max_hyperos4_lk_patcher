package com.android.services.telephony.rcs.validator;

/* JADX INFO: loaded from: classes.dex */
public class ValidationResult {
    public static final ValidationResult SUCCESS = new ValidationResult();
    public final boolean isValidated;
    public final String logReason;
    public final int restrictedReason;

    private ValidationResult() {
        this.isValidated = true;
        this.restrictedReason = 0;
        this.logReason = "";
    }

    public ValidationResult(int i, String str) {
        this.isValidated = false;
        this.restrictedReason = i;
        this.logReason = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ValidationResult{validated=");
        sb.append(this.isValidated);
        if (!this.isValidated) {
            sb.append(", restrictedReason=");
            sb.append(this.restrictedReason);
            sb.append(", logReason=");
            sb.append(this.logReason);
        }
        sb.append('}');
        return sb.toString();
    }
}
