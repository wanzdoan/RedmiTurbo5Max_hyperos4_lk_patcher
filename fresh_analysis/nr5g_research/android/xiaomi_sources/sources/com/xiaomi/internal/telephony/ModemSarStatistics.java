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
    private static Context mContext;
    private static ModemSarStatisticsHandler mHandler;
    private static HandlerThread mHandlerThread;
    private static int mHorizontalGameState;
    private static ModemSarStatistics mInstance;
    private static long mLasHorizontalGametDsiTime;
    private static int mLastDsiValue = -1;
    private static ConcurrentHashMap<Integer, ConcurrentHashMap<String, Long>> mDsiStatisticMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Integer> mDsiRegisterMap = new ConcurrentHashMap<>();

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
        mHandlerThread = new HandlerThread("RadStatisticsBgThread");
        mHandlerThread.start();
        mHandler = new ModemSarStatisticsHandler(mHandlerThread.getLooper());
    }

    private static class ModemSarStatisticsHandler extends Handler {
        public ModemSarStatisticsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ModemSarLog.d(ModemSarStatistics.LOG_TAG, "handlerMessage, msg = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            byte[] bytes = (byte[]) ar.result;
            ByteBuffer buffer = ModemUtils.getBuffer(bytes);
            if (buffer == null) {
                ModemSarLog.d(ModemSarStatistics.LOG_TAG, "handlerMessage, buffer is null, do nothing");
            }
            switch (msg.what) {
                case 1:
                    int state = buffer.getInt();
                    if (state != ModemSarStatistics.mHorizontalGameState) {
                        ModemSarStatistics.mHorizontalGameState = state;
                        long currentTime = SystemClock.elapsedRealtime();
                        if (ModemSarStatistics.mHorizontalGameState == 1) {
                            ModemSarStatistics.registerDsiValueChangeEvent(this, 1);
                            ModemSarStatistics.mLasHorizontalGametDsiTime = currentTime;
                        } else {
                            ModemSarStatistics.unregisterDsiValueChangeEvent(this, 1);
                            if (ModemSarStatistics.mLasHorizontalGametDsiTime != 0) {
                                ModemSarStatistics.recordDsiStatistic(1, (currentTime - ModemSarStatistics.mLasHorizontalGametDsiTime) / 1000, ModemSarStatistics.mLastDsiValue);
                            }
                            ModemSarStatistics.mLasHorizontalGametDsiTime = 0L;
                            ModemSarStatistics.reportHorizontalGameDsiStatistic();
                            ModemSarStatistics.clearDsiStatistic(1);
                        }
                    }
                    break;
                case 2:
                    int dsiValue = buffer.getInt();
                    if (dsiValue == ModemSarStatistics.mLastDsiValue) {
                        ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, dsiValue = " + dsiValue + ", mLastDsiValue = " + ModemSarStatistics.mLastDsiValue + ", do nothing");
                    } else {
                        ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, dsiValue = " + dsiValue + ", mLastDsiValue = " + ModemSarStatistics.mLastDsiValue);
                        long currentTime2 = SystemClock.elapsedRealtime();
                        if (ModemSarStatistics.mLastDsiValue != -1 && ModemSarStatistics.mLasHorizontalGametDsiTime != 0) {
                            long interval = (currentTime2 - ModemSarStatistics.mLasHorizontalGametDsiTime) / 1000;
                            ModemSarStatistics.recordDsiStatistic(1, interval, ModemSarStatistics.mLastDsiValue);
                            ModemSarStatistics.mLasHorizontalGametDsiTime = currentTime2;
                            ModemSarLog.i(ModemSarStatistics.LOG_TAG, "EVENT_DSI_VALUE_CHANGE, recordDsiStatistic, dsiStatisticEventId = 1, interval = " + interval + ", dsi = " + ModemSarStatistics.mLastDsiValue);
                        }
                        ModemSarStatistics.mLastDsiValue = dsiValue;
                    }
                    break;
            }
        }
    }

    public static void reportTxWhitelistScancodeData(HashMap<String, String> txWhitelistMap) {
        ModemSarLog.i(LOG_TAG, "reportWhitelistData!");
        Bundle txWhitelistBundle = new Bundle();
        ArrayList<String> txWhitelistKeyList = new ArrayList<>();
        for (Map.Entry<String, String> entry : txWhitelistMap.entrySet()) {
            txWhitelistKeyList.add(entry.getKey());
            txWhitelistBundle.putString(entry.getKey(), entry.getValue());
        }
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, "telephony_statistic");
        intent.putExtra(EXTRA_EVENT_NAME, KEY_TX_WHITELIST_SCANCODE_STATISTIC);
        txWhitelistBundle.putStringArrayList(MAP_KEY_NAME_LIST, txWhitelistKeyList);
        intent.putExtra(EXTRA_PARAM_NAME, txWhitelistBundle);
        mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportHorizontalGameDsiStatistic() {
        ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic");
        ConcurrentHashMap<String, Long> map = mDsiStatisticMap.get(1);
        if (map == null || map.isEmpty()) {
            ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic, map is null or empty");
            return;
        }
        ModemSarLog.i(LOG_TAG, "reportHorizontalGameDsiStatistic, map = " + map + ", topGame = " + GameStateListener.HorizontalGameListListener.getCurrentTopGame());
        Bundle bundle = new Bundle();
        ArrayList<String> keyNameList = new ArrayList<>();
        keyNameList.add("TopGame");
        bundle.putString("TopGame", GameStateListener.HorizontalGameListListener.getCurrentTopGame());
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            keyNameList.add(entry.getKey());
            bundle.putString(entry.getKey(), Long.toString(entry.getValue().longValue()));
        }
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, "telephony_statistic");
        intent.putExtra(EXTRA_EVENT_NAME, KEY_HORIZONTAL_GAME_DSI_STATISTIC);
        bundle.putStringArrayList(MAP_KEY_NAME_LIST, keyNameList);
        intent.putExtra(EXTRA_PARAM_NAME, bundle);
        mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void clearDsiStatistic(int dsiStatisticEventId) {
        ModemSarLog.i(LOG_TAG, "clearDsiStatistic, dsiStatisticEventId = " + dsiStatisticEventId);
        ConcurrentHashMap<String, Long> map = mDsiStatisticMap.get(Integer.valueOf(dsiStatisticEventId));
        if (map != null) {
            map.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void recordDsiStatistic(int dsiStatisticEventId, long interval, int dsi) {
        ModemSarLog.i(LOG_TAG, "recordDsiStatistic, dsiStatisticEventId = " + dsiStatisticEventId + ", interval = " + interval + ", dsi = " + dsi);
        ConcurrentHashMap<String, Long> map = mDsiStatisticMap.get(Integer.valueOf(dsiStatisticEventId));
        if (map == null) {
            map = new ConcurrentHashMap<>();
            mDsiStatisticMap.put(Integer.valueOf(dsiStatisticEventId), map);
        }
        long lastInterval = map.getOrDefault("" + dsi, 0L).longValue();
        map.put("" + dsi, Long.valueOf(lastInterval + interval));
    }

    private static boolean checkDsiRegisterState(int dsiStatisticEventId) {
        boolean isRegistered = false;
        for (Map.Entry<Integer, Integer> entry : mDsiRegisterMap.entrySet()) {
            if (!entry.getKey().equals(Integer.valueOf(dsiStatisticEventId))) {
                int currentValue = entry.getValue().intValue();
                if (currentValue == 1) {
                    isRegistered = true;
                }
            }
        }
        ModemSarLog.i(LOG_TAG, "checkDsiRegisterState, isRegistered = " + isRegistered);
        return isRegistered;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerDsiValueChangeEvent(Handler h, int dsiStatisticEventId) {
        ModemSarLog.i(LOG_TAG, "registerDsiValueChangeEvent, h = " + h + ", eventId = " + dsiStatisticEventId);
        if (!checkDsiRegisterState(dsiStatisticEventId) && (mDsiRegisterMap.get(Integer.valueOf(dsiStatisticEventId)) == null || mDsiRegisterMap.get(Integer.valueOf(dsiStatisticEventId)).intValue() == 0)) {
            ModemSarLog.i(LOG_TAG, "registerDsiValueChangeEvent, registerDsiValueChangeEvent");
            SarDeviceStateListener.registerDeviceStateChangeEvent(mContext, 29, h, 2, null);
        }
        mDsiRegisterMap.put(Integer.valueOf(dsiStatisticEventId), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void unregisterDsiValueChangeEvent(Handler h, int dsiStatisticEventId) {
        ModemSarLog.i(LOG_TAG, "unregisterDsiValueChangeEvent, h = " + h + ", eventId = " + dsiStatisticEventId);
        if (!checkDsiRegisterState(dsiStatisticEventId) && mDsiRegisterMap.get(Integer.valueOf(dsiStatisticEventId)) != null && mDsiRegisterMap.get(Integer.valueOf(dsiStatisticEventId)).intValue() == 1) {
            ModemSarLog.i(LOG_TAG, "unregisterDsiValueChangeEvent, unregisterDsiValueChangeEvent");
            SarDeviceStateListener.unregisterDeviceStateChangeEvent(29, h);
        }
        mDsiRegisterMap.put(Integer.valueOf(dsiStatisticEventId), 0);
    }
}
