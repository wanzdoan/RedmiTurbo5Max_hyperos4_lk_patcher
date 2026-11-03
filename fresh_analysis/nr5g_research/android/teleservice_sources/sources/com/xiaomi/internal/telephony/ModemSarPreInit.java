package com.xiaomi.internal.telephony;

import android.content.Context;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.util.List;
import java.util.Optional;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarPreInit {
    private static final String LOG_TAG = "ModemRadPreInit";
    private static final int SAR_VERSION = 2;
    private static final String UNCOUPLE_SAR_DEVICES = "breeze";
    private static ModemSarPreInit sInstance;

    public static void make(Context context, OemHookAgent oemHookAgent) {
        ModemSarLog.init(context);
        if (sInstance == null) {
            sInstance = new ModemSarPreInit(context, oemHookAgent);
        }
    }

    private ModemSarPreInit(Context context, OemHookAgent oemHookAgent) {
        ModemSarLog.d(LOG_TAG, "PreInit start");
        String deviceName = ModemUtils.getDeviceName();
        ModemSarLog.d(LOG_TAG, "Start to check starting condition, deviceName = " + deviceName);
        startModemSarIfNeeded(context, oemHookAgent, ModemSarUtils.readSarXmlBySAX(ModemSarUtils.PATH_SAR_CONFIG), deviceName);
        startNtnSarIfNeeded(context, ModemSarUtils.readSarXmlBySAX(ModemSarUtils.PATH_NTN_SAR_CONFIG));
    }

    private void startModemSarIfNeeded(Context context, OemHookAgent oemHookAgent, XMLContentHandler xMLContentHandler, String str) {
        if (isModemSarNeeded() && xMLContentHandler != null && xMLContentHandler.getDeviceSarConfig() != null) {
            String str2 = (String) Optional.ofNullable(xMLContentHandler.getSarVersion()).orElse("0");
            List deviceSarConfig = xMLContentHandler.getDeviceSarConfig();
            try {
                if (Integer.parseInt(str2) == 2 || UNCOUPLE_SAR_DEVICES.contains(str)) {
                    DeviceSarConfig deviceSarConfigInitModemSarConfig = ModemSarUtils.initModemSarConfig(deviceSarConfig, ModemSarUtils.getModemSarSwitch());
                    ModemSarStatistics.init(context);
                    if (2 == OemHookAgent.getHookType() && deviceSarConfigInitModemSarConfig != null) {
                        QcSarController.make(context, oemHookAgent, deviceSarConfigInitModemSarConfig);
                        return;
                    }
                    if (1 == OemHookAgent.getHookType() && deviceSarConfigInitModemSarConfig != null) {
                        if (isMtkTaSarConfig(deviceSarConfigInitModemSarConfig)) {
                            MtkTaSarController.make(context, oemHookAgent, deviceSarConfigInitModemSarConfig);
                            return;
                        } else {
                            MtkGroupSarController.make(context, oemHookAgent, deviceSarConfigInitModemSarConfig);
                            return;
                        }
                    }
                    if (3 == OemHookAgent.getHookType() && deviceSarConfigInitModemSarConfig != null) {
                        XringMtkTaSarController.make(context, oemHookAgent, deviceSarConfigInitModemSarConfig);
                        return;
                    } else {
                        ModemSarLog.e(LOG_TAG, "Make instance failed!!!");
                        return;
                    }
                }
                ModemSarLog.e(LOG_TAG, "The modem feature is not supported!!!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "get modem version failed!!!");
                return;
            }
        }
        ModemSarLog.e(LOG_TAG, "Start modem feature failed!!!");
    }

    private void startNtnSarIfNeeded(Context context, XMLContentHandler xMLContentHandler) {
        if (isNtnSarNeeded() && xMLContentHandler != null && xMLContentHandler.getSarVersion() != null && xMLContentHandler.getDeviceSarConfig() != null) {
            String sarVersion = xMLContentHandler.getSarVersion();
            List deviceSarConfig = xMLContentHandler.getDeviceSarConfig();
            try {
                if (Integer.parseInt(sarVersion) >= 2) {
                    DeviceSarConfig deviceSarConfigInitModemSarConfig = ModemSarUtils.initModemSarConfig(deviceSarConfig, ModemSarUtils.getNtnSarSwitch());
                    if (deviceSarConfigInitModemSarConfig != null) {
                        NtnSarController.make(context, deviceSarConfigInitModemSarConfig);
                        return;
                    } else {
                        ModemSarLog.e(LOG_TAG, "Make instance failed!!!");
                        return;
                    }
                }
                ModemSarLog.e(LOG_TAG, "The ntn feature is not supported!!!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ModemSarLog.e(LOG_TAG, "get ntn version failed!!!");
                return;
            }
        }
        ModemSarLog.e(LOG_TAG, "Start ntn feature failed!!!");
    }

    private boolean isMtkTaSarConfig(DeviceSarConfig deviceSarConfig) {
        boolean z = true;
        if (1 == OemHookAgent.getHookType()) {
            if (deviceSarConfig != null && !deviceSarConfig.getAntennaGroup().equals("null")) {
                z = false;
            }
        } else {
            ModemSarLog.e(LOG_TAG, "Error, this is qc platform");
        }
        ModemSarLog.d(LOG_TAG, "TA support status = " + z);
        return z;
    }

    private boolean isModemSarNeeded() {
        if (!ModemSarUtils.isModemSarSwitchOff() && !ModemSarUtils.isFactoryBuild()) {
            return true;
        }
        ModemSarLog.e(LOG_TAG, "This device is not supported for modem");
        return false;
    }

    private boolean isNtnSarNeeded() {
        if (!ModemSarUtils.isNtnSarSwitchOff() && !ModemSarUtils.isFactoryBuild()) {
            return true;
        }
        ModemSarLog.e(LOG_TAG, "This device is not supported for ntn");
        return false;
    }
}
