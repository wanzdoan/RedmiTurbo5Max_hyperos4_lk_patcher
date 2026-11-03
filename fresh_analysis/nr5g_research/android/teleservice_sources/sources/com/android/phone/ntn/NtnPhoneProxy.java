package com.android.phone.ntn;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.ServiceState;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.phone.PhoneGlobals;
import com.android.phone.SatelliteManagerBase;
import com.android.phone.utils.MiuiSatelliteUtils;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneProxy extends NtnPhoneProxyBase {
    public static final Context sContext = PhoneGlobals.getInstance();

    public static NtnPhoneProxyBase make() {
        if (NtnPhoneProxyBase.mInstance instanceof NtnPhoneProxy) {
            return NtnPhoneProxyBase.mInstance;
        }
        NtnPhoneProxyBase.mInstance = new NtnPhoneProxy();
        return NtnPhoneProxyBase.mInstance;
    }

    private NtnPhoneProxy() {
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public boolean startNtnPhone() {
        try {
            int phoneId = PhoneFactory.getDefaultPhone().getPhoneId();
            NtnPhoneFactory.makeDefaultPhone(sContext, phoneId);
            NtnPhone ntnPhone = getNtnPhone();
            if (ntnPhone == null) {
                return false;
            }
            ntnPhone.initNtnRadio();
            MiuiSatelliteUtils.setupOnNtnBoot();
            NtnLogUtils.logd("NtnPhoneProxy", "startNtnPhone successfully, phoneId=" + phoneId);
            return true;
        } catch (Exception e) {
            NtnLogUtils.loge("NtnPhoneProxy", "startNtnPhone: " + e);
            return false;
        }
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public boolean destroyNtnPhone() {
        NtnPhone ntnPhone = getNtnPhone();
        if (ntnPhone == null) {
            return false;
        }
        Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0);
        SatelliteManagerBase.getInstance().sendEnableSatelliteBroadcast(ntnPhone.getPhoneId(), false, false);
        ntnPhone.shutdownRadio();
        ntnPhone.destroyNtnRadio();
        NtnLogUtils.logd("NtnPhoneProxy", "destroyNtnPhone: destroy radio successfully!");
        return true;
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
        try {
            NtnPhone ntnPhone = getNtnPhone();
            if (ntnPhone == null) {
                return;
            }
            NtnLogUtils.logd("NtnPhoneProxy", "setRadioPower power=" + z);
            ntnPhone.setRadioPower(z, z2, z3, z4);
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", z ? 1 : 0);
            if (z4) {
                SatelliteManagerBase.getInstance().sendEnableSatelliteBroadcast(ntnPhone.getPhoneId(), z, false);
            }
        } catch (Exception e) {
            NtnLogUtils.loge("NtnPhoneProxy", "setRadioPower: " + e);
        }
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public int getRadioState() {
        NtnRIL ntnRIL;
        NtnPhone ntnPhone = getNtnPhone();
        int radioState = (ntnPhone == null || (ntnRIL = ntnPhone.mNtnRil) == null) ? 2 : ntnRIL.getRadioState();
        NtnLogUtils.logd("NtnPhoneProxy", "getRadioState state=" + radioState);
        return radioState;
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public Phone getNtnPhone() {
        try {
            try {
                return NtnPhoneFactory.getDefaultPhone();
            } catch (Exception e) {
                NtnLogUtils.loge("NtnPhoneProxy", "get ntnphones failed, exception: " + e);
                return null;
            }
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public boolean isNtnInService() {
        Phone ntnPhone = getNtnPhone();
        boolean z = false;
        if (ntnPhone != null && ntnPhone.getServiceState() != null) {
            ServiceState serviceState = ntnPhone.getServiceState();
            z = serviceState.getState() == 0;
            NtnLogUtils.logd("NtnPhoneProxy", "isNtnInService isInService:" + z + ", serviceState:" + serviceState.getState());
        }
        return z;
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public void registerForRadioStateChanged(Handler handler, int i, Object obj) {
        NtnPhone ntnPhone = getNtnPhone();
        if (ntnPhone != null) {
            ntnPhone.mNtnRil.registerForRadioStateChanged(handler, i, obj);
        }
    }

    @Override // com.android.phone.ntn.NtnPhoneProxyBase
    public void unregisterForRadioStateChanged(Handler handler) {
        NtnPhone ntnPhone = getNtnPhone();
        if (ntnPhone != null) {
            ntnPhone.mNtnRil.unregisterForRadioStateChanged(handler);
        }
    }
}
