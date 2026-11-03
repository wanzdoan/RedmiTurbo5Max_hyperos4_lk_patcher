package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.IModemModule;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.ot.pubsub.g.f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class EarlyInitStatistic {
    private static volatile EarlyInitStatistic sInstance;
    private final Context mContext;
    private Handler mHandler;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.EarlyInitStatistic.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("miui.intent.action.MODEMENHANCE_STATISTIC".equals(intent.getAction())) {
                EarlyInitStatistic.this.mHandler.obtainMessage(1, intent).sendToTarget();
            }
        }
    };

    private EarlyInitStatistic(Context context) {
        this.mContext = context;
        initHandler();
        registerReceiver();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (EarlyInitStatistic.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new EarlyInitStatistic(context);
                        logD("init done");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private void initHandler() {
        this.mHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.statistics.EarlyInitStatistic.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    EarlyInitStatistic.this.handleModemEnhanceStatEvent((Intent) message.obj);
                }
            }
        };
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.MODEMENHANCE_STATISTIC");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        logD("registerReceiver done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleModemEnhanceStatEvent(Intent intent) {
        if (intent == null) {
            logE("handleModemEnhanceStatEvent intent is null");
            return;
        }
        HashMap map = new HashMap();
        String stringExtra = intent.getStringExtra("category_name");
        String stringExtra2 = intent.getStringExtra(f.a.l);
        Object extra = intent.getExtra("param_name");
        if (stringExtra == null || stringExtra2 == null || extra == null) {
            logE("handleModemEnhanceStatEvent null parameter");
            return;
        }
        if ("modemenhance_statistic_fatal_device".equals(stringExtra2)) {
            logD("handleModemEnhanceStatEvent handle ME_FATAL_DEVICE_STATISTIC to ModemDFS");
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).getRfFatalDeviceStatus();
        }
        if ("modemenhance_statistic_fatal_device_dbg".equals(stringExtra2)) {
            IModemModule iModemModule = (IModemModule) ModuleDelegate.get(IModemModule.class);
            iModemModule.reportRfDeviceStatus();
            iModemModule.reportRfMainChipStatus();
            return;
        }
        if ("modemenhance_statistic_tx_fbrx_fault_dbg".equals(stringExtra2)) {
            IModemModule iModemModule2 = (IModemModule) ModuleDelegate.get(IModemModule.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("key_name_list", new ArrayList<>(Arrays.asList("txagc", "techId", "band", "txFreq", "carrierIdx", "gainState", "pwrErrCode", "sigPath", "cumulativeErrCount")));
            bundle.putString("txagc", "-100");
            bundle.putString("techId", "9");
            bundle.putString("band", "78");
            bundle.putString("txFreq", "3600000000");
            bundle.putString("carrierIdx", "0");
            bundle.putString("gainState", "2");
            bundle.putString("pwrErrCode", "3");
            bundle.putString("sigPath", "0");
            bundle.putString("cumulativeErrCount", "1");
            iModemModule2.reportTxFbrxFault(bundle);
            return;
        }
        if ("modemenhance_statistic_tx_fbrx_fault".equals(stringExtra2)) {
            logD("handleModemEnhanceStatEvent handle TX_FBRX_FAULT to ModemDFS");
            IModemModule iModemModule3 = (IModemModule) ModuleDelegate.get(IModemModule.class);
            if (extra instanceof Bundle) {
                iModemModule3.reportTxFbrxFault((Bundle) extra);
                return;
            }
            return;
        }
        if (extra instanceof ArrayList) {
            logD("handleModemEnhanceStatEvent handle ArrayList");
            map.put("Params", (ArrayList) extra);
        } else if (extra instanceof Bundle) {
            logD("handleModemEnhanceStatEvent handle Bundle");
            Bundle bundle2 = (Bundle) extra;
            ArrayList<String> stringArrayList = bundle2.getStringArrayList("key_name_list");
            if (stringArrayList == null || stringArrayList.isEmpty()) {
                logE("handleModemEnhanceStatEvent null keys");
                return;
            }
            logD("handleModemEnhanceStatEvent category:" + stringExtra + "   event=" + stringExtra2);
            for (int i = 0; i < stringArrayList.size(); i++) {
                String string = bundle2.getString(stringArrayList.get(i));
                logD("handleModemEnhanceStatEvent key:" + stringArrayList.get(i) + "   value=" + string);
                map.put(stringArrayList.get(i), string);
            }
        } else {
            logE("handleModemEnhanceStatEvent received unknown type for EXTRA_PARAM_NAME");
            return;
        }
        logD("handleModemEnhanceStatEvent :" + map);
        MiStatInterfaceUtil.recordObjectEvent(stringExtra, stringExtra2, map);
    }

    private static void logD(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("EarlyInitStatistic", str);
        }
    }

    private static void logE(String str) {
        Rlog.e("EarlyInitStatistic", str);
    }
}
