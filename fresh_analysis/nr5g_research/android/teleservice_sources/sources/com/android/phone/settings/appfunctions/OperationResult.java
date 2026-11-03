package com.android.phone.settings.appfunctions;

/* JADX INFO: loaded from: classes.dex */
public class OperationResult {
    private final String actualValue;
    private final String resultDesc;

    public OperationResult(String str, String str2) {
        this.resultDesc = str;
        this.actualValue = str2;
    }

    public String getResultDesc() {
        return this.resultDesc;
    }

    public String getActualValue() {
        return this.actualValue;
    }
}
