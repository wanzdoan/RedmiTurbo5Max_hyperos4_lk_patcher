package com.android.phone;

import android.util.Log;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyActionBase {
    protected ConcurrentHashMap mActionMaps = new ConcurrentHashMap();

    interface TeleAction {
        boolean revertAction(ActionReason actionReason, Object... objArr);

        boolean takeAction(ActionReason actionReason, Object... objArr);
    }

    public enum ActionReason {
        REASON_NONE(0),
        CLOUD_CELL(1),
        CLOUD_PUSH(2),
        SELF_STUDY(3),
        MODEM_CTRL(4),
        WECHAT_OPT(5),
        GAME_OPT(6);

        private final int val;

        ActionReason(int i) {
            this.val = i;
        }

        public int value() {
            return this.val;
        }

        public static ActionReason fromValue(int i) {
            for (ActionReason actionReason : values()) {
                if (actionReason.val == i) {
                    return actionReason;
                }
            }
            return REASON_NONE;
        }
    }

    public boolean teleAction(int i, ActionReason actionReason, Object... objArr) {
        int intPreference;
        if (PhoneDebug.VDBG) {
            Log.d("TelephonyAction", "teleAction " + i + " " + actionReason);
        }
        if (this.mActionMaps.containsKey(Integer.valueOf(i))) {
            try {
                boolean zTakeAction = ((TeleAction) this.mActionMaps.get(Integer.valueOf(i))).takeAction(actionReason, objArr);
                if (zTakeAction) {
                    int iOrdinal = actionReason.ordinal();
                    if (iOrdinal == 1) {
                        intPreference = PreferenceUtils.getInstance().getIntPreference("cell_version");
                    } else {
                        intPreference = iOrdinal != 4 ? 0 : PreferenceUtils.getInstance().getIntPreference("CloudMdCtrl.mdctrl_cloud_ver");
                    }
                    HashMap map = new HashMap();
                    map.put("action", String.valueOf(i));
                    map.put("reason", actionReason.name());
                    map.put("version", String.valueOf(intPreference));
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "TeleAction", map);
                }
                return zTakeAction;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean revertTeleAction(int i, ActionReason actionReason, Object... objArr) {
        if (PhoneDebug.VDBG) {
            StringBuilder sb = new StringBuilder("revertAction ");
            sb.append(i);
            sb.append(" ");
            sb.append(actionReason);
            sb.append(" arg:");
            sb.append(objArr.length > 0 ? objArr[0] : null);
            Log.d("TelephonyAction", sb.toString());
        }
        if (this.mActionMaps.containsKey(Integer.valueOf(i))) {
            try {
                return ((TeleAction) this.mActionMaps.get(Integer.valueOf(i))).revertAction(actionReason, objArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
