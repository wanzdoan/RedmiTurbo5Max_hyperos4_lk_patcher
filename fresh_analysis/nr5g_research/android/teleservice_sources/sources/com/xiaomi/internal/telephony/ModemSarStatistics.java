package com.xiaomi.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModemSarStatistics {
    private static final String ACTION_MODEMENHANCE_STATISTIC = "miui.intent.action.MODEMENHANCE_STATISTIC";
    private static final int DSI_STATISTIC_HORIZON_GAME_LIST = 1;
    public static final int EVENT_DSI_VALUE_CHANGE = 2;
    public static final int EVENT_HORIZONTAL_GAME_STATE_CHANGE = 1;
    private static final String EXTRA_CATEGORY_NAME = "category_name";
    private static final String EXTRA_EVENT_NAME = "event_name";
    private static final String EXTRA_PARAM_NAME = "param_name";
    public static final String KEY_HORIZONTAL_GAME_DSI_STATISTIC = "sar_horizontal_game_dsi_statistic";
    public static final String KEY_TX_WHITELIST_SCANCODE_STATISTIC = "sar_tx_whitelist_scancode_statistic";
    private static final String LOG_TAG = "RadStatistics";
    private static final String MAP_KEY_NAME_LIST = "key_name_list";
    private static final String ME_STATISTIC_CATEGORY = "telephony_statistic";
    private static Context mContext = null;
    private static ModemSarStatisticsHandler mHandler = null;
    private static HandlerThread mHandlerThread = null;
    private static int mHorizontalGameState = 0;
    private static ModemSarStatistics mInstance = null;
    private static long mLasHorizontalGametDsiTime = 0;
    private static int mLastDsiValue = -1;
    private static ConcurrentHashMap mDsiStatisticMap = new ConcurrentHashMap();
    private static ConcurrentHashMap mDsiRegisterMap = new ConcurrentHashMap();

    public static void init(Context context) {
        mContext = context;
        if (mHandler == null) {
            mInstance = new ModemSarStatistics();
        }
    }

    public static Handler getHandler() {
        return mHandler;
    }

    private ModemSarStatistics() {
        HandlerThread handlerThread = new HandlerThread("RadStatisticsBgThread");
        mHandlerThread = handlerThread;
        handlerThread.start();
        mHandler = new ModemSarStatisticsHandler(mHandlerThread.getLooper());
    }

    class ModemSarStatisticsHandler extends Handler {
        public ModemSarStatisticsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemSarLog.d(ModemSarStatistics.LOG_TAG, "handlerMessage, msg = " + message.what);
            ByteBuffer buffer = ModemUtils.getBuffer((byte[]) ((AsyncResult) message.obj).result);
            if (buffer == null) {
                ModemSarLog.d(ModemSarStatistics.LOG_TAG, "handlerMessage, buffer is null, do nothing");
                return;
            }
            int i = message.what;
            if (i == 1) {
                int i2 = buffer.getInt();
                if (i2 != ModemSarStatistics.mHorizontalGameState) {
                    ModemSarStatistics.mHorizontalGameState = i2;
                    long jElapsedRealtime = SystemClock.elapsedRealtime();
                    if (ModemSarStatistics.mHorizontalGameState == 1) {
                        ModemSarStatistics.registerDsiValueChangeEvent(this, 1);
                        ModemSarStatistics.mLasHorizontalGametDsiTime = jElapsedRealtime;
                        return;
                    }
                    ModemSarStatistics.unregisterDsiValueChangeEvent(this, 1);
                    if (ModemSarStatistics.mLasHorizontalGametDsiTime != 0) {
                        ModemSarStatistics.recordDsiStatistic(1, (jElapsedRealtime - ModemSarStatistics.mLasHorizontalGametDsiTime) / 1000, ModemSarStatistics.mLastDsiValue);
                    }
                    ModemSarStatistics.mLasHorizontalGametDsiTime = 0L;
                    ModemSarStatistics.reportHorizontalGameDsiStatistic();
                    ModemSarStatistics.clearDsiStatistic(1);
                    return;
                }
                return;
            }
            if (i != 2) {
                return;
            }
            int i3 = buffer.getInt();
            if (i3 == ModemSarStatistics.mLastDsiValue) {
                ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, dsiValue = " + i3 + ", mLastDsiValue = " + ModemSarStatistics.mLastDsiValue + ", do nothing");
                return;
            }
            ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, dsiValue = " + i3 + ", mLastDsiValue = " + ModemSarStatistics.mLastDsiValue);
            long jElapsedRealtime2 = SystemClock.elapsedRealtime();
            if (ModemSarStatistics.mLastDsiValue != -1 && ModemSarStatistics.mLasHorizontalGametDsiTime != 0) {
                long j = (jElapsedRealtime2 - ModemSarStatistics.mLasHorizontalGametDsiTime) / 1000;
                ModemSarStatistics.recordDsiStatistic(1, j, ModemSarStatistics.mLastDsiValue);
                ModemSarStatistics.mLasHorizontalGametDsiTime = jElapsedRealtime2;
                ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, recordDsiStatistic, dsiStatisticEventId = 1, interval = " + j + ", dsi = " + ModemSarStatistics.mLastDsiValue);
            }
            ModemSarStatistics.mLastDsiValue = i3;
        }
    }

    public static void reportTxWhitelistScancodeData(HashMap map) {
        ModemSarLog.i(LOG_TAG, "reportWhitelistData!");
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            arrayList.add((String) entry.getKey());
            bundle.putString((String) entry.getKey(), (String) entry.getValue());
        }
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, "telephony_statistic");
        intent.putExtra("event_name", KEY_TX_WHITELIST_SCANCODE_STATISTIC);
        bundle.putStringArrayList(MAP_KEY_NAME_LIST, arrayList);
        intent.putExtra(EXTRA_PARAM_NAME, bundle);
        mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportHorizontalGameDsiStatistic() {
        ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic");
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) mDsiStatisticMap.get(1);
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic, map is null or empty");
            return;
        }
        ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic, map = " + concurrentHashMap + ", topGame = " + GameStateListener.HorizontalGameListListener.getCurrentTopGame());
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("TopGame");
        bundle.putString("TopGame", GameStateListener.HorizontalGameListListener.getCurrentTopGame());
        for (Map.Entry entry : concurrentHashMap.entrySet()) {
            arrayList.add((String) entry.getKey());
            bundle.putString((String) entry.getKey(), Long.toString(((Long) entry.getValue()).longValue()));
        }
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, "telephony_statistic");
        intent.putExtra("event_name", KEY_HORIZONTAL_GAME_DSI_STATISTIC);
        bundle.putStringArrayList(MAP_KEY_NAME_LIST, arrayList);
        intent.putExtra(EXTRA_PARAM_NAME, bundle);
        mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void clearDsiStatistic(int i) {
        ModemSarLog.i(LOG_TAG, "clearDsiStatistic, dsiStatisticEventId = " + i);
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) mDsiStatisticMap.get(Integer.valueOf(i));
        if (concurrentHashMap != null) {
            concurrentHashMap.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recordDsiStatistic(int i, long j, int i2) {
        ModemSarLog.i(LOG_TAG, "recordDsiStatistic, dsiStatisticEventId = " + i + ", interval = " + j + ", dsi = " + i2);
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) mDsiStatisticMap.get(Integer.valueOf(i));
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            mDsiStatisticMap.put(Integer.valueOf(i), concurrentHashMap);
        }
        concurrentHashMap.put("" + i2, Long.valueOf(((Long) concurrentHashMap.getOrDefault("" + i2, 0L)).longValue() + j));
    }

    private static boolean checkDsiRegisterState(int i) {
        boolean z = false;
        for (Map.Entry entry : mDsiRegisterMap.entrySet()) {
            if (!((Integer) entry.getKey()).equals(Integer.valueOf(i)) && ((Integer) entry.getValue()).intValue() == 1) {
                z = true;
            }
        }
        ModemSarLog.i(LOG_TAG, "checkDsiRegisterState, isRegistered = " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerDsiValueChangeEvent(Handler handler, int i) {
        ModemSarLog.i(LOG_TAG, "registerDsiValueChangeEvent, h = " + handler + ", eventId = " + i);
        if (!checkDsiRegisterState(i) && (mDsiRegisterMap.get(Integer.valueOf(i)) == null || ((Integer) mDsiRegisterMap.get(Integer.valueOf(i))).intValue() == 0)) {
            ModemSarLog.i(LOG_TAG, "registerDsiValueChangeEvent, registerDsiValueChangeEvent");
            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 29, handler, 2, null);
        }
        mDsiRegisterMap.put(Integer.valueOf(i), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void unregisterDsiValueChangeEvent(Handler handler, int i) {
        ModemSarLog.i(LOG_TAG, "unregisterDsiValueChangeEvent, h = " + handler + ", eventId = " + i);
        if (!checkDsiRegisterState(i) && mDsiRegisterMap.get(Integer.valueOf(i)) != null && ((Integer) mDsiRegisterMap.get(Integer.valueOf(i))).intValue() == 1) {
            ModemSarLog.i(LOG_TAG, "unregisterDsiValueChangeEvent, unregisterDsiValueChangeEvent");
            SarDeviceStateListener.unregisterDeviceStateChangeEvent(29, handler);
        }
        mDsiRegisterMap.put(Integer.valueOf(i), 0);
    }
}
