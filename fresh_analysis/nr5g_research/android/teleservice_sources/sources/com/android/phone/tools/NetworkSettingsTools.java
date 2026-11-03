package com.android.phone.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.aios.apptoolsdk.AppResult;
import com.android.internal.telephony.IccCardActivateHelper;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.settings.MobileNetworkSettings;
import java.util.concurrent.CountDownLatch;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkSettingsTools {
    private static final String KEY_VICE_SLOT_VOLTE_DATA = MobileNetworkSettings.VICE_SLOT_VOLTE_DATA_ENABLED;

    public static AppResult networkSettings(Context context, String str, String str2, Integer num) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("action 不能为空，支持：get / set_sim_enabled / set_sim_name / set_default_call_sim / set_default_data_sim / set_mobile_data / set_smart_dual_sim / set_smart_callback / set_smart_transfer");
        }
        String lowerCase = str.trim().toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase) {
            case "set_default_data_sim":
                return setDefaultDataSim(context, num);
            case "set_smart_callback":
                return setSmartCallback(context, str2);
            case "set_sim_enabled":
                return setSimEnabled(num, str2);
            case "set_smart_transfer":
                return setSmartTransfer(context, str2);
            case "get":
                return getNetworkSettings(context, num);
            case "set_sim_name":
                return setSimName(num, str2);
            case "set_smart_dual_sim":
                return setSmartDualSim(context, str2);
            case "set_mobile_data":
                return setMobileData(context, str2);
            case "set_default_call_sim":
                return setDefaultCallSim(context, num);
            default:
                return AppResult.error("无效的 action：" + str + "，支持：get / set_sim_enabled / set_sim_name / set_default_call_sim / set_default_data_sim / set_mobile_data / set_smart_dual_sim / set_smart_callback / set_smart_transfer");
        }
    }

    private static AppResult getNetworkSettings(Context context, Integer num) {
        StringBuilder sb = new StringBuilder("{");
        if (num == null) {
            sb.append("\"sim1\":");
            sb.append(getSimInfoJson(0));
            sb.append(",\"sim2\":");
            sb.append(getSimInfoJson(1));
            sb.append(",");
        } else {
            sb.append("\"sim");
            sb.append(num.intValue() + 1);
            sb.append("\":");
            sb.append(getSimInfoJson(num.intValue()));
            sb.append(",");
        }
        int defaultVoiceSlotId = DefaultSimManager.getDefaultVoiceSlotId();
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        boolean z = telephonyManager != null && telephonyManager.isDataEnabled();
        boolean smartDualSimState = getSmartDualSimState(context);
        boolean z2 = MiuiSettings.System.getBoolean(context.getContentResolver(), "use_last_sim_callback", false);
        boolean zIsSmartCallForwardEnabled = MiuiSettings.Telephony.isSmartCallForwardEnabled(context.getContentResolver());
        if (defaultVoiceSlotId < 0) {
            sb.append("\"default_call_sim\":\"ask_every_time\",");
        } else {
            sb.append("\"default_call_sim\":");
            sb.append(defaultVoiceSlotId);
            sb.append(",");
        }
        sb.append("\"default_data_sim\":");
        sb.append(defaultDataSlotId);
        sb.append(",\"mobile_data\":");
        sb.append(z);
        sb.append(",\"smart_dual_sim\":");
        sb.append(smartDualSimState);
        sb.append(",\"smart_callback\":");
        sb.append(z2);
        sb.append(",\"smart_transfer\":");
        sb.append(zIsSmartCallForwardEnabled);
        sb.append("}");
        return AppResult.success(sb.toString());
    }

    private static String getSimInfoJson(int i) {
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot == null) {
            return "{\"inserted\":false}";
        }
        return "{\"inserted\":true,\"enabled\":" + MiuiPhoneUtils.isIccCardActivated(i) + ",\"name\":\"" + ToolUtils.escapeJson(availableSubscriptionInfoBySlot.getDisplayName() != null ? availableSubscriptionInfoBySlot.getDisplayName().toString() : "") + "\",\"number\":\"" + ToolUtils.escapeJson(availableSubscriptionInfoBySlot.getDisplayNumber() != null ? availableSubscriptionInfoBySlot.getDisplayNumber() : "") + "\",\"subscription_id\":" + availableSubscriptionInfoBySlot.getSubscriptionId() + "}";
    }

    /* JADX WARN: Code duplicated, block: B:40:0x0072  */
    private static AppResult setSimEnabled(Integer num, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_sim_enabled 需要 value 参数（true=启用，false=禁用）");
        }
        boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(str.trim());
        boolean z = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(0) != null;
        boolean z2 = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1) != null;
        if (num == null) {
            if (zEqualsIgnoreCase) {
                boolean z3 = z && !MiuiPhoneUtils.isIccCardActivated(0);
                boolean z4 = z2 && !MiuiPhoneUtils.isIccCardActivated(1);
                if (z3 && !z4) {
                    num = 0;
                } else {
                    if (!z4 || z3) {
                        return AppResult.error("请通过 sim_slot 指定要启用的卡（0=SIM1，1=SIM2）");
                    }
                    num = 1;
                }
            } else {
                if (z && z2) {
                    return AppResult.error("请通过 sim_slot 指定要禁用的卡（0=SIM1，1=SIM2）");
                }
                if (z) {
                    num = 0;
                } else {
                    if (!z2) {
                        return AppResult.error("未检测到已插入的 SIM 卡");
                    }
                    num = 1;
                }
            }
        }
        if (!zEqualsIgnoreCase) {
            if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(num.intValue()) == null) {
                return AppResult.error("SIM" + (num.intValue() + 1) + " 未插卡或不可用");
            }
            int i = num.intValue() == 0 ? 1 : 0;
            if (MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i) == null) {
                return AppResult.error("当前只有一张 SIM 卡，不允许禁用");
            }
            if (!MiuiPhoneUtils.isIccCardActivated(i)) {
                return AppResult.error("SIM" + (i + 1) + " 已被禁用，不允许同时禁用 SIM" + (num.intValue() + 1));
            }
            boolean z5 = DefaultSimManager.getDefaultVoiceSlotId() == num.intValue();
            boolean z6 = DefaultSimManager.getDefaultDataSlotId() == num.intValue();
            if (z5 && z6) {
                StringBuilder sb = new StringBuilder("SIM");
                sb.append(num.intValue() + 1);
                sb.append(" 是当前的默认拨号卡和默认上网卡，禁用前请先完成以下操作：\n1. 通过 set_default_call_sim 将默认拨号卡切换到 SIM");
                int i2 = i + 1;
                sb.append(i2);
                sb.append("\n2. 通过 set_default_data_sim 将默认上网卡切换到 SIM");
                sb.append(i2);
                return AppResult.error(sb.toString());
            }
            if (z5) {
                return AppResult.error("SIM" + (num.intValue() + 1) + " 是当前的默认拨号卡，禁用前请先通过 set_default_call_sim 将默认拨号卡切换到 SIM" + (i + 1));
            }
            if (z6) {
                return AppResult.error("SIM" + (num.intValue() + 1) + " 是当前的默认上网卡，禁用前请先通过 set_default_data_sim 将默认上网卡切换到 SIM" + (i + 1));
            }
        }
        try {
            IccCardActivateHelper.setIccCardActivate(num.intValue(), zEqualsIgnoreCase);
            return AppResult.success("{\"sim_slot\":" + num + ",\"enabled\":" + zEqualsIgnoreCase + "}");
        } catch (Exception e) {
            return AppResult.error("设置 SIM" + (num.intValue() + 1) + " 状态失败：" + e.getMessage());
        }
    }

    private static AppResult setSimName(Integer num, String str) {
        if (num == null) {
            return AppResult.error("set_sim_name 需要 sim_slot 参数（0=SIM1，1=SIM2）");
        }
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_sim_name 需要 value 参数（新名称）");
        }
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(num.intValue());
        if (availableSubscriptionInfoBySlot == null) {
            return AppResult.error("SIM" + (num.intValue() + 1) + " 未插卡或不可用");
        }
        try {
            int displayNameForSubscription = SubscriptionManagerEx.getDefault().setDisplayNameForSubscription(str.trim(), availableSubscriptionInfoBySlot.getSubscriptionId());
            if (displayNameForSubscription < 0) {
                return AppResult.error("修改卡名失败，返回码：" + displayNameForSubscription);
            }
            return AppResult.success("{\"sim_slot\":" + num + ",\"name\":\"" + ToolUtils.escapeJson(str.trim()) + "\"}");
        } catch (Exception e) {
            return AppResult.error("修改 SIM" + (num.intValue() + 1) + " 卡名失败：" + e.getMessage());
        }
    }

    private static AppResult setDefaultCallSim(Context context, Integer num) {
        if (num == null) {
            return AppResult.error("set_default_call_sim 需要 sim_slot 参数（0=SIM1，1=SIM2，-1=每次询问）");
        }
        try {
            DefaultSimManager.getInstance().setDefaultVoiceSlotId(num.intValue(), context.getPackageName());
            return AppResult.success("{\"default_call_sim\":\"" + (num.intValue() < 0 ? "ask_every_time" : String.valueOf(num)) + "\"}");
        } catch (Exception e) {
            return AppResult.error("设置默认拨号卡失败：" + e.getMessage());
        }
    }

    private static AppResult setDefaultDataSim(Context context, Integer num) {
        if (num == null) {
            return AppResult.error("set_default_data_sim 需要 sim_slot 参数（0=SIM1，1=SIM2）");
        }
        if (!isWifiConnected(context)) {
            return AppResult.error("当前未连接 Wi-Fi，切换上网卡会导致网络中断，指令无法继续执行。请先连接 Wi-Fi 后再切换默认上网卡。");
        }
        try {
            DefaultSimManager.getInstance().setDefaultDataSlotId(num.intValue(), context.getPackageName());
            return AppResult.success("{\"default_data_sim\":" + num + "}");
        } catch (Exception e) {
            return AppResult.error("设置默认上网卡失败：" + e.getMessage());
        }
    }

    private static AppResult setMobileData(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_mobile_data 需要 value 参数（true=开启，false=关闭）");
        }
        boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(str.trim());
        try {
            TelephonyManager.from(context).setDataEnabled(zEqualsIgnoreCase);
            return AppResult.success("{\"mobile_data\":" + zEqualsIgnoreCase + "}");
        } catch (Exception e) {
            return AppResult.error("设置移动数据失败：" + e.getMessage());
        }
    }

    private static boolean getSmartDualSimState(Context context) {
        if (MiuiPhoneUtils.isDualDataSupported()) {
            return Settings.Global.getInt(context.getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(context.getContentResolver(), KEY_VICE_SLOT_VOLTE_DATA, 0) == 1 && Settings.Global.getInt(context.getContentResolver(), "dual_mobile_data", 0) == 1;
        }
        return MiuiPhoneUtils.isSmartSwitchDualSimSupported() && Settings.Global.getInt(context.getContentResolver(), "smart_dual_sim", 0) == 1 && Settings.Global.getInt(context.getContentResolver(), KEY_VICE_SLOT_VOLTE_DATA, 0) == 1;
    }

    private static AppResult setSmartDualSim(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_smart_dual_sim 需要 value 参数（true 或 false）");
        }
        boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(str.trim());
        if (MiuiPhoneUtils.isDualDataSupported()) {
            Settings.Global.putInt(context.getContentResolver(), "smart_dual_sim", zEqualsIgnoreCase ? 1 : 0);
            Settings.Global.putInt(context.getContentResolver(), KEY_VICE_SLOT_VOLTE_DATA, zEqualsIgnoreCase ? 1 : 0);
            Settings.Global.putInt(context.getContentResolver(), "dual_mobile_data", zEqualsIgnoreCase ? 1 : 0);
        } else if (MiuiPhoneUtils.isSmartSwitchDualSimSupported()) {
            Settings.Global.putInt(context.getContentResolver(), "smart_dual_sim", zEqualsIgnoreCase ? 1 : 0);
            Settings.Global.putInt(context.getContentResolver(), KEY_VICE_SLOT_VOLTE_DATA, zEqualsIgnoreCase ? 1 : 0);
        } else {
            return AppResult.error("该设备不支持智能双卡/智能切换上网卡功能");
        }
        return AppResult.success("{\"smart_dual_sim\":" + zEqualsIgnoreCase + "}");
    }

    private static AppResult setSmartCallback(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_smart_callback 需要 value 参数（true 或 false）");
        }
        boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(str.trim());
        MiuiSettings.System.putBoolean(context.getContentResolver(), "use_last_sim_callback", zEqualsIgnoreCase);
        return AppResult.success("{\"smart_callback\":" + zEqualsIgnoreCase + "}");
    }

    private static AppResult setSmartTransfer(Context context, String str) {
        if (str == null || str.trim().isEmpty()) {
            return AppResult.error("set_smart_transfer 需要 value 参数（true 或 false）");
        }
        if (!"true".equalsIgnoreCase(str.trim())) {
            MiuiSettings.Telephony.setSmartCallForwardEnabled(context.getContentResolver(), false);
            clearSmartCFNumbers(context);
            sendSmartCFDisableBestEffort(context);
            return AppResult.success("{\"smart_transfer\":false}");
        }
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(0);
        SubscriptionInfo availableSubscriptionInfoBySlot2 = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1);
        if (availableSubscriptionInfoBySlot == null || availableSubscriptionInfoBySlot2 == null) {
            return AppResult.error("智能互转需要双卡，当前未检测到双卡");
        }
        String displayNumber = availableSubscriptionInfoBySlot.getDisplayNumber();
        String displayNumber2 = availableSubscriptionInfoBySlot2.getDisplayNumber();
        if (TextUtils.isEmpty(displayNumber) || TextUtils.isEmpty(displayNumber2)) {
            StringBuilder sb = new StringBuilder("无法读取 SIM 卡号码（SIM1=");
            sb.append(TextUtils.isEmpty(displayNumber) ? "空" : "✓");
            sb.append("，SIM2=");
            sb.append(TextUtils.isEmpty(displayNumber2) ? "空" : "✓");
            sb.append("），请通过手机设置界面手动开启智能互转并确认转接号码");
            return AppResult.error(sb.toString());
        }
        Phone phone = ToolUtils.getPhone(0);
        Phone phone2 = ToolUtils.getPhone(1);
        if (phone == null || phone2 == null) {
            return AppResult.error("无法获取 Phone 对象");
        }
        AsyncResult asyncResultDoSetSmartCFRequest = doSetSmartCFRequest(phone, 3, displayNumber2, 1);
        String str2 = "（超时）";
        if (asyncResultDoSetSmartCFRequest == null || asyncResultDoSetSmartCFRequest.exception != null) {
            if (asyncResultDoSetSmartCFRequest != null && asyncResultDoSetSmartCFRequest.exception != null) {
                str2 = "：" + asyncResultDoSetSmartCFRequest.exception.getMessage();
            }
            return AppResult.error("设置 SIM1 来电转接失败".concat(str2));
        }
        AsyncResult asyncResultDoSetSmartCFRequest2 = doSetSmartCFRequest(phone2, 3, displayNumber, 2);
        if (asyncResultDoSetSmartCFRequest2 == null || asyncResultDoSetSmartCFRequest2.exception != null) {
            doSetSmartCFRequest(phone, 0, null, 3);
            StringBuilder sb2 = new StringBuilder("设置 SIM2 来电转接失败");
            if (asyncResultDoSetSmartCFRequest2 != null && asyncResultDoSetSmartCFRequest2.exception != null) {
                str2 = "：" + asyncResultDoSetSmartCFRequest2.exception.getMessage();
            }
            sb2.append(str2);
            sb2.append("，已回滚 SIM1 设置");
            return AppResult.error(sb2.toString());
        }
        MiuiSettings.Telephony.setSmartCallForwardEnabled(context.getContentResolver(), true);
        storeSmartCFNumbers(context, displayNumber2, displayNumber);
        return AppResult.success("{\"smart_transfer\":true}");
    }

    private static AsyncResult doSetSmartCFRequest(Phone phone, int i, String str, int i2) {
        Object obj;
        final Object[] objArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        HandlerThread handlerThread = new HandlerThread("miclaw-smartcf-" + i2);
        handlerThread.start();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallForwardingOption(phone, i, 3, str, 1, 0, new Handler(handlerThread.getLooper()) { // from class: com.android.phone.tools.NetworkSettingsTools.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                objArr[0] = message.obj;
                countDownLatch.countDown();
            }
        }.obtainMessage(i2));
        boolean zAwaitLatch = ToolUtils.awaitLatch(countDownLatch, 30000L);
        handlerThread.quit();
        if (!zAwaitLatch || (obj = objArr[0]) == null) {
            return null;
        }
        return (AsyncResult) obj;
    }

    private static void sendSmartCFDisableBestEffort(Context context) {
        new Thread(new Runnable() { // from class: com.android.phone.tools.NetworkSettingsTools$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NetworkSettingsTools.m4133$r8$lambda$oiq2f2SPvTvQ3HivxxsnXq8T64();
            }
        }, "miclaw-smartcf-disable").start();
    }

    /* JADX INFO: renamed from: $r8$lambda$oiq2f2S-PvTvQ3HivxxsnXq8T64, reason: not valid java name */
    public static /* synthetic */ void m4133$r8$lambda$oiq2f2SPvTvQ3HivxxsnXq8T64() {
        Phone phone = ToolUtils.getPhone(0);
        Phone phone2 = ToolUtils.getPhone(1);
        if (phone != null) {
            doSetSmartCFRequest(phone, 0, null, 10);
        }
        if (phone2 != null) {
            doSetSmartCFRequest(phone2, 0, null, 11);
        }
    }

    private static void storeSmartCFNumbers(Context context, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = "<null>";
        }
        sb.append(str);
        sb.append(",");
        if (str2 == null) {
            str2 = "<null>";
        }
        sb.append(str2);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("key_telephony_last_stored_number", sb.toString()).apply();
    }

    private static void clearSmartCFNumbers(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("key_telephony_last_stored_number").apply();
    }

    private static boolean isWifiConnected(Context context) {
        Network activeNetwork;
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return (connectivityManager == null || (activeNetwork = connectivityManager.getActiveNetwork()) == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null || !networkCapabilities.hasTransport(1)) ? false : true;
    }
}
