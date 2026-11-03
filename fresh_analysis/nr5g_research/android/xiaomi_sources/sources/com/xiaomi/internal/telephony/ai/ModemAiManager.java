package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModemAiManager {
    private static final String LOG_TAG = "ModemAiManager";
    private static Context mContext;
    private OemHookAgent.IHookReadyCallBack mCb = new OemHookAgent.IHookReadyCallBack() { // from class: com.xiaomi.internal.telephony.ai.ModemAiManager.1
        @Override // com.xiaomi.modem.OemHookAgent.IHookReadyCallBack
        public void onHookReady(OemHookAgent hook) {
            ModemAiManager.log("ModemAiManager onHookReady, hook=" + hook);
            if (hook != null) {
                ModemAiManager.this.mHookAgent = hook;
            } else {
                ModemAiManager.log("hook is null");
            }
        }
    };
    private OemHookAgent mHookAgent;
    private static ModemAiManager mInstance = null;
    private static SignalModel mSignalModel = null;
    private static GameModel mGameModel = null;

    private ModemAiManager(Context cnt) {
        log("creat ModemAiManager Obj");
        mContext = cnt;
        OemHookAgent.registerHookReadyCallback(this.mCb, cnt);
        mSignalModel = new SignalModel(cnt);
        mGameModel = new GameModel(cnt);
    }

    public static ModemAiManager getInstance(Context cnt) {
        log("on ModemAiManager getInstance");
        if (mInstance == null) {
            mInstance = new ModemAiManager(cnt);
        }
        return mInstance;
    }

    public void runSignalModel() {
        if (this.mHookAgent == null || mSignalModel == null) {
            return;
        }
        mSignalModel.startRun(this.mHookAgent);
    }

    public void stopSignalModel() {
        if (this.mHookAgent == null || mSignalModel == null) {
            return;
        }
        mSignalModel.stopRun(this.mHookAgent);
    }

    public List<HashMap<String, Object>> getSignalModelOneDayIndicator() {
        if (mSignalModel == null) {
            return new ArrayList();
        }
        return mSignalModel.getSignalModelOneDayIndicator();
    }

    public void runGameModel(String game_pkg_name, int curGameScene, int curGameRat) {
        if (this.mHookAgent == null || mGameModel == null) {
            return;
        }
        mGameModel.startRun(this.mHookAgent, game_pkg_name, curGameScene, curGameRat);
    }

    public void stopGameModel() {
        if (this.mHookAgent == null || mGameModel == null) {
            return;
        }
        mGameModel.stopRun(this.mHookAgent);
    }

    public void setGameDelay(int delay) {
        if (mGameModel == null) {
            return;
        }
        mGameModel.setDelayBuf(delay);
    }

    public void setGameRat(int rat) {
        if (mGameModel == null) {
            return;
        }
        mGameModel.setGameRat(rat);
    }

    public List<HashMap<String, Object>> getGameModelOneDayIndicator() {
        if (mGameModel == null) {
            return new ArrayList();
        }
        return mGameModel.getGameModelOneDayIndicator();
    }

    public ConcurrentHashMap<String, ArrayList> getGameModelOneDayParam() {
        if (mGameModel == null) {
            return new ConcurrentHashMap<>();
        }
        return mGameModel.getGameModelOneDayParam();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String msg) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, msg);
    }
}
