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
    private static ModemSarPreInit sInstance = null;

    public static void make(Context context, OemHookAgent hookAgent) {
        ModemSarLog.init(context);
        if (sInstance == null) {
            sInstance = new ModemSarPreInit(context, hookAgent);
        }
    }

    private ModemSarPreInit(Context context, OemHookAgent hookAgent) {
        ModemSarLog.d(LOG_TAG, "PreInit start");
        String deviceName = ModemUtils.getDeviceName();
        ModemSarLog.d(LOG_TAG, "Start to check starting condition, deviceName = " + deviceName);
        startModemSarIfNeeded(context, hookAgent, ModemSarUtils.readSarXmlBySAX(ModemSarUtils.PATH_SAR_CONFIG), deviceName);
        startNtnSarIfNeeded(context, ModemSarUtils.readSarXmlBySAX(ModemSarUtils.PATH_NTN_SAR_CONFIG));
    }

    private void startModemSarIfNeeded(Context context, OemHookAgent hookAgent, XMLContentHandler handler, String deviceName) {
        if (isModemSarNeeded() && handler != null && handler.getDeviceSarConfig() != null) {
            String sarVersion = (String) Optional.ofNullable(handler.getSarVersion()).orElse("0");
            List<DeviceSarConfig> deviceSarConfigList = handler.getDeviceSarConfig();
            try {
                int sarVersionInt = Integer.parseInt(sarVersion);
                if (sarVersionInt == 2 || UNCOUPLE_SAR_DEVICES.contains(deviceName)) {
                    DeviceSarConfig deviceSarConfig = ModemSarUtils.initModemSarConfig(deviceSarConfigList, ModemSarUtils.getModemSarSwitch());
                    ModemSarStatistics.init(context);
                    if (2 == OemHookAgent.getHookType() && deviceSarConfig != null) {
                        QcSarController.make(context, hookAgent, deviceSarConfig);
                        return;
                    }
                    if (1 == OemHookAgent.getHookType() && deviceSarConfig != null) {
                        if (isMtkTaSarConfig(deviceSarConfig)) {
                            MtkTaSarController.make(context, hookAgent, deviceSarConfig);
                            return;
                        } else {
                            MtkGroupSarController.make(context, hookAgent, deviceSarConfig);
                            return;
                        }
                    }
                    if (3 == OemHookAgent.getHookType() && deviceSarConfig != null) {
                        XringMtkTaSarController.make(context, hookAgent, deviceSarConfig);
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

    private void startNtnSarIfNeeded(Context context, XMLContentHandler handler) {
        if (isNtnSarNeeded() && handler != null && handler.getSarVersion() != null && handler.getDeviceSarConfig() != null) {
            String sarVersion = handler.getSarVersion();
            List<DeviceSarConfig> deviceSarConfigList = handler.getDeviceSarConfig();
            try {
                int sarVersionInt = Integer.parseInt(sarVersion);
                if (sarVersionInt >= 2) {
                    DeviceSarConfig deviceSarConfig = ModemSarUtils.initModemSarConfig(deviceSarConfigList, ModemSarUtils.getNtnSarSwitch());
                    if (deviceSarConfig != null) {
                        NtnSarController.make(context, deviceSarConfig);
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
        boolean result = true;
        if (1 == OemHookAgent.getHookType()) {
            if (deviceSarConfig != null && !deviceSarConfig.getAntennaGroup().equals("null")) {
                result = false;
            }
        } else {
            ModemSarLog.e(LOG_TAG, "Error, this is qc platform");
        }
        ModemSarLog.d(LOG_TAG, "TA support status = " + result);
        return result;
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
