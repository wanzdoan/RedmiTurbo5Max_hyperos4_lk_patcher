package com.android.phone.settings.appfunctions;

/* JADX INFO: loaded from: classes.dex */
public enum DeviceStateAppFunctionType {
    GET_UNCATEGORIZED("getUncategorizedDeviceState"),
    GET_METADATA("getDeviceStateMetadata"),
    SET_DEVICE_STATE("setDeviceStateItem");

    private final String functionId;

    DeviceStateAppFunctionType(String str) {
        this.functionId = str;
    }

    public String getFunctionId() {
        return this.functionId;
    }

    public static DeviceStateAppFunctionType fromId(String str) {
        for (DeviceStateAppFunctionType deviceStateAppFunctionType : values()) {
            if (deviceStateAppFunctionType.functionId.equals(str)) {
                return deviceStateAppFunctionType;
            }
        }
        return null;
    }
}
