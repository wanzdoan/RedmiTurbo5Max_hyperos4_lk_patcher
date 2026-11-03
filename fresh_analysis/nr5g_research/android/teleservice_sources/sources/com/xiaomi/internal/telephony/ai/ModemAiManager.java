package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModemAiManager {
    private static final String LOG_TAG = "ModemAiManager";
    private static Context mContext;
    private static GameModel mGameModel;
    private static ModemAiManager mInstance;
    private static SignalModel mSignalModel;
    private OemHookAgent.IHookReadyCallBack mCb = new OemHookAgent.IHookReadyCallBack() { // from class: com.xiaomi.internal.telephony.ai.ModemAiManager.1
        @Override // com.xiaomi.modem.OemHookAgent.IHookReadyCallBack
        public void onHookReady(OemHookAgent oemHookAgent) {
            ModemAiManager.log("ModemAiManager onHookReady, hook=" + oemHookAgent);
            if (oemHookAgent != null) {
                ModemAiManager.this.mHookAgent = oemHookAgent;
            } else {
                ModemAiManager.log("hook is null");
            }
        }
    };
    private OemHookAgent mHookAgent;

    private ModemAiManager(Context context) {
        log("creat ModemAiManager Obj");
        mContext = context;
        OemHookAgent.registerHookReadyCallback(this.mCb, context);
        mSignalModel = new SignalModel(context);
        mGameModel = new GameModel(context);
    }

    public static ModemAiManager getInstance(Context context) {
        log("on ModemAiManager getInstance");
        if (mInstance == null) {
            mInstance = new ModemAiManager(context);
        }
        return mInstance;
    }

    public void runSignalModel() {
        SignalModel signalModel;
        OemHookAgent oemHookAgent = this.mHookAgent;
        if (oemHookAgent == null || (signalModel = mSignalModel) == null) {
            return;
        }
        signalModel.startRun(oemHookAgent);
    }

    public void stopSignalModel() {
        SignalModel signalModel;
        OemHookAgent oemHookAgent = this.mHookAgent;
        if (oemHookAgent == null || (signalModel = mSignalModel) == null) {
            return;
        }
        signalModel.stopRun(oemHookAgent);
    }

    public List getSignalModelOneDayIndicator() {
        SignalModel signalModel = mSignalModel;
        if (signalModel == null) {
            return new ArrayList();
        }
        return signalModel.getSignalModelOneDayIndicator();
    }

    public void runGameModel(String str, int i, int i2) {
        GameModel gameModel;
        OemHookAgent oemHookAgent = this.mHookAgent;
        if (oemHookAgent == null || (gameModel = mGameModel) == null) {
            return;
        }
        gameModel.startRun(oemHookAgent, str, i, i2);
    }

    public void stopGameModel() {
        GameModel gameModel;
        OemHookAgent oemHookAgent = this.mHookAgent;
        if (oemHookAgent == null || (gameModel = mGameModel) == null) {
            return;
        }
        gameModel.stopRun(oemHookAgent);
    }

    public void setGameDelay(int i) {
        GameModel gameModel = mGameModel;
        if (gameModel == null) {
            return;
        }
        gameModel.setDelayBuf(i);
    }

    public void setGameRat(int i) {
        GameModel gameModel = mGameModel;
        if (gameModel == null) {
            return;
        }
        gameModel.setGameRat(i);
    }

    public List getGameModelOneDayIndicator() {
        GameModel gameModel = mGameModel;
        if (gameModel == null) {
            return new ArrayList();
        }
        return gameModel.getGameModelOneDayIndicator();
    }

    public ConcurrentHashMap getGameModelOneDayParam() {
        GameModel gameModel = mGameModel;
        if (gameModel == null) {
            return new ConcurrentHashMap();
        }
        return gameModel.getGameModelOneDayParam();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String str) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, str);
    }
}
