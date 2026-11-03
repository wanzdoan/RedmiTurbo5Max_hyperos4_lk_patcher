package com.mediatek.phone.ext;

/* JADX INFO: loaded from: classes.dex */
public class OpPhoneCustomizationFactoryBase {
    public IIncomingCallExt makeIncomingCallExt() {
        return new DefaultIncomingCallExt();
    }

    public IMobileNetworkSettingsExt makeMobileNetworkSettingsExt() {
        return new DefaultMobileNetworkSettingsExt();
    }
}
