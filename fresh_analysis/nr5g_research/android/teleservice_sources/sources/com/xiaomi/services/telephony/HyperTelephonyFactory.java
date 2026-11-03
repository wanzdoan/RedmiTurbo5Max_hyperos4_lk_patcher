package com.xiaomi.services.telephony;

import com.android.phone.MiCarrierConfigLoaderImpl;
import com.android.phone.MiCarrierConfigLoaderStub;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.MiuiPhoneApp;
import com.android.services.telephony.MiTelephonyServiceImpl;
import com.android.services.telephony.MiTelephonyServiceStub;

/* JADX INFO: loaded from: classes2.dex */
public class HyperTelephonyFactory implements TelephonyFactory.IHyperInterface {
    @Override // com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface
    public MiTelephonyServiceStub.IMiTelephonyService makeTelephonyServiceProxy() {
        return new MiTelephonyServiceImpl();
    }

    @Override // com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface
    public MiCarrierConfigLoaderStub.IMiCarrierConfigLoader makeCcLoaderProxy() {
        return new MiCarrierConfigLoaderImpl();
    }

    @Override // com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface
    public MiPhoneAppStub.IMiPhoneApp makePhoneAppProxy() {
        return new MiuiPhoneApp();
    }
}
