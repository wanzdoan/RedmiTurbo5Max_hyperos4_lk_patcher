package com.android.internal.telephony.ntnphone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.SystemProperties;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DefaultPhoneNotifier;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.utils.NtnBoxOptimization;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.telephony.Rlog;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneFactory {
    protected static final String PROP_SAT_RESTART_RILD_DISABLED = "0";
    protected static final String PROP_SAT_RESTART_RILD_PROP = "persist.radio.ntn_rild_slot";
    private static CommandsInterface[] sCommandsInterfaces = null;
    private static boolean sMadeDefaults = false;
    private static NtnRIL sNtnCi;
    private static NtnPhone sNtnPhone;
    private static PhoneNotifier sPhoneNotifier;
    private static Phone[] sPhones;
    static final Object sLockProxyPhone = new Object();
    private static FeatureFlags sFeatureFlags = new FeatureFlagsImpl();

    public static void makeDefaultPhone(Context context, int i) {
        Context context2;
        synchronized (sLockProxyPhone) {
            try {
                if (!sMadeDefaults) {
                    FeatureFlags featureFlags = PhoneFactory.getFeatureFlags();
                    sFeatureFlags = featureFlags;
                    sPhoneNotifier = new DefaultPhoneNotifier(context, featureFlags);
                    sNtnCi = new NtnRIL(context, (RIL) PhoneFactory.getPhone(i).mCi);
                    context2 = context;
                    sNtnPhone = new NtnPhone(context2, sNtnCi, sPhoneNotifier, i, sFeatureFlags);
                    Phone[] phoneArr = (Phone[]) Arrays.copyOf(PhoneFactory.getPhones(), PhoneFactory.getPhones().length);
                    sPhones = phoneArr;
                    if (phoneArr.length > i) {
                        phoneArr[i] = sNtnPhone;
                    }
                    sCommandsInterfaces = (CommandsInterface[]) Arrays.copyOf(PhoneFactory.getCommandsInterfaces(), PhoneFactory.getCommandsInterfaces().length);
                    sMadeDefaults = true;
                } else {
                    context2 = context;
                    updateSatellitePhone(sNtnPhone, i);
                }
                if (MiuiTelephonyManagerStub.isSatelliteBox()) {
                    NtnBoxOptimization.init(context2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static Phone updateSatellitePhone(Phone phone, int i) {
        if (TelephonyCommonUtils.isNtnChipVendor_54() || sNtnPhone.isHardSimSwapForTT()) {
            CommandsInterface[] commandsInterfaceArr = sCommandsInterfaces;
            if (commandsInterfaceArr != null && commandsInterfaceArr.length > i) {
                commandsInterfaceArr[i] = sNtnCi;
            }
            UiccController.getInstance().updateUiccCommandsInterface(sCommandsInterfaces, true);
        }
        if (i != phone.getPhoneId()) {
            sNtnPhone.setPhoneId(i);
            sNtnPhone.setPowerSwitch(false, 0);
        }
        return phone;
    }

    public static Phone getDefaultPhone() {
        NtnPhone ntnPhone;
        NtnPhone ntnPhone2 = sNtnPhone;
        if (ntnPhone2 == null || ntnPhone2.getSatelliteConfig(SatelliteConfig.Type.TIANTONG_1) == null) {
            SomeArgs$$ExternalSyntheticBUOutline0.m("not support satellite!");
            return null;
        }
        synchronized (sLockProxyPhone) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                ntnPhone = sNtnPhone;
            } catch (Throwable th) {
                throw th;
            }
        }
        return ntnPhone;
    }

    public static Phone getNtnPhone() {
        NtnPhone ntnPhone;
        synchronized (sLockProxyPhone) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                ntnPhone = sNtnPhone;
            } catch (Throwable th) {
                throw th;
            }
        }
        return ntnPhone;
    }

    public static Phone[] getPhones() {
        Phone[] phoneArr;
        synchronized (sLockProxyPhone) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                phoneArr = sPhones;
            } catch (Throwable th) {
                throw th;
            }
        }
        return phoneArr;
    }

    public static void BackToCellular() {
        synchronized (sLockProxyPhone) {
            try {
                if (TelephonyCommonUtils.isNtnChipVendor_54() || sNtnPhone.isHardSimSwapForTT()) {
                    sCommandsInterfaces = (CommandsInterface[]) Arrays.copyOf(PhoneFactory.getCommandsInterfaces(), PhoneFactory.getCommandsInterfaces().length);
                    UiccController.getInstance().updateUiccCommandsInterface(sCommandsInterfaces, false);
                    Rlog.i("PhoneFactory", "stopSatellite: set celluar sim power up!");
                    setSimPowerState(1);
                }
                if (!MiuiTelephonyManagerStub.isSatelliteBox() && !TelephonyCommonUtils.isNtnChipVendor_54() && !sNtnPhone.isHardSimSwapForTT()) {
                    ((ConnectivityManager) PhoneFactory.getDefaultPhone().getContext().getSystemService("connectivity")).setAirplaneMode(false);
                    SystemProperties.set("persist.radio.ntn_airplane_on", "false");
                    for (Phone phone : PhoneFactory.getPhones()) {
                        Rlog.i("PhoneFactory", "stopSatellite check cell radio state : " + phone.mCi.getRadioState());
                        if (phone.mCi.getRadioState() == 2) {
                            phone.setRadioPower(true, false, false, true);
                        }
                    }
                }
                SystemProperties.set(PROP_SAT_RESTART_RILD_PROP, PROP_SAT_RESTART_RILD_DISABLED);
                Rlog.i("PhoneFactory", "stopSatellite: close satellite stack successfully!");
                if (MiuiTelephonyManagerStub.isSatelliteBox() && NtnBoxOptimization.getInstance() != null) {
                    NtnBoxOptimization.getInstance().destroy();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void BackToCellularForCitTest() {
        synchronized (sLockProxyPhone) {
            try {
                if (TelephonyCommonUtils.isNtnChipVendor_54() || sNtnPhone.isHardSimSwapForTT()) {
                    UiccController.getInstance().updateUiccCommandsInterface(sCommandsInterfaces, false);
                    Rlog.i("PhoneFactory", "stopSatellite: set celluar sim power up!");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Rlog.i("PhoneFactory", "destroyDefaultPhoneForCitTest: close CIT Test successfully!");
    }

    public static void destroyDefaultPhoneForCitTest() {
        Rlog.i("PhoneFactory", "destroyDefaultPhoneForCitTest: close CIT Test successfully!");
    }

    private static void setSimPowerState(int i) {
        TelephonyManager telephonyManager = (TelephonyManager) PhoneFactory.getDefaultPhone().getContext().getSystemService("phone");
        for (Phone phone : PhoneFactory.getPhones()) {
            telephonyManager.setSimPowerStateForSlot(phone.getPhoneId(), i);
        }
    }
}
