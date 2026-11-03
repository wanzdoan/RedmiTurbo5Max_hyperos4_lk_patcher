package com.android.phone.networkmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.Rlog;
import java.lang.reflect.Field;
import java.util.List;
import miui.enterprise.EnterpriseManagerStub;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class EpNetworkModeUpdateReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (!EnterpriseManagerStub.ENTERPRISE_ACTIVATED) {
            Rlog.e("EpNetworkModeUpdateReceiver", "enterprise not activated");
            return;
        }
        if (intent.getBooleanExtra("ep_network_mode_update", false)) {
            try {
                DefaultSimManager defaultSimManager = DefaultSimManager.getInstance();
                Field declaredField = DefaultSimManager.class.getDeclaredField("mDataSlotListener");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(defaultSimManager);
                if (obj instanceof List) {
                    for (Object obj2 : (List) obj) {
                        if (obj2 != null) {
                            if (obj2 instanceof NetworkModeHandlerMtkDual4G) {
                                ((NetworkModeHandlerMtkDual4G) obj2).onDataSlotReady(true);
                            } else if (obj2 instanceof NetworkModeHandlerAllSlot) {
                                ((NetworkModeHandlerAllSlot) obj2).onDataSlotReady(true);
                            } else if (obj2 instanceof NetworkModeHandlerDataSlot) {
                                ((NetworkModeHandlerDataSlot) obj2).onDataSlotReady(true);
                            } else if (obj2 instanceof NetworkModeHandlerBase) {
                                ((NetworkModeHandlerBase) obj2).onDataSlotReady(true);
                            }
                        }
                    }
                    return;
                }
                Rlog.e("EpNetworkModeUpdateReceiver", "mDataSlotListener is not of type List<DataSlotListener>");
            } catch (Exception e) {
                e.printStackTrace();
                Rlog.e("EpNetworkModeUpdateReceiver", "enterprise ep_network_mode_update_receive error:" + e);
            }
        }
    }
}
