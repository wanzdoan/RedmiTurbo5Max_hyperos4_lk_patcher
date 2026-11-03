package com.android.phone.eventhub.events;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class EventTypes$Broadcast {
    private static final Map ACTION_TO_SUBTYPE;

    static {
        HashMap map = new HashMap();
        ACTION_TO_SUBTYPE = map;
        map.put("android.intent.action.BATTERY_CHANGED", "battery_changed");
        map.put("android.intent.action.BOOT_COMPLETED", "boot_completed");
        map.put("android.intent.action.AIRPLANE_MODE", "airplane_mode_changed");
        map.put("android.intent.action.ACTION_IMS_REGISTED", "ims_registed");
        map.put("android.intent.action.SIM_STATE_CHANGED", "sim_state_changed");
        map.put("android.telephony.action.CARRIER_CONFIG_CHANGED", "carrier_config_changed");
        map.put("android.telephony.action.SIM_CARD_STATE_CHANGED", "sim_card_state_changed");
        map.put("com.android.phone.intent.action.WECHAT_VOIP_SCENE", "wechat_voip_scene");
        map.put("com.miui.powerkeeper_sleep_changed", "power_keeper_sleep_changed");
    }

    public static String getSubTypeFromAction(String str) {
        return (String) ACTION_TO_SUBTYPE.getOrDefault(str, str);
    }
}
