package com.android.phone;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.gwsd.GwsdListener;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class GwsdManager extends GwsdManagerBase {
    private static String LOG_TAG = "GwsdManager";
    private Context mContext;
    private ExtTelephonyManager mExtTelephonyManager;
    private int mGwsdAutoRejectRetryTime;
    private ContentObserver mGwsdContentObserver;
    private boolean mGwsdTargetEnabled;
    private int mGwsdUserSelectRetryTime;
    private boolean mIsHyperEngineOptSupported;
    private final int MSG_GWSD_USER_SELECT = 0;
    private final int MSG_GWSD_AUTO_REJECT = 1;
    private final int MSG_DEFAULT_DATA_SWITCHED = 2;
    private final int MSG_DEFAULT_DSDA_CHANGED = 3;
    private final int MSG_SYSTEM_STATE_MODEM_RESET = 4;
    private final int MSG_SET_CALL_VALID_TIMER = 5;
    private final int MSG_IGNORE_SAME_CALL_INTERVAL = 6;
    private final int MSG_SYSTEM_STATE_OUT_OF_SERVICE = 7;
    private GwsdListenerImpl mGwsdListenerImpl = new GwsdListenerImpl();
    private Handler mHandler = new Handler() { // from class: com.android.phone.GwsdManager.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                if (message.arg1 == 0) {
                    GwsdManager.this.mGwsdUserSelectRetryTime = 0;
                    return;
                }
                if ("can not configure mode on non default data sim when device is under dsds".equals((String) message.obj)) {
                    return;
                }
                int i2 = GwsdManager.this.mGwsdUserSelectRetryTime;
                GwsdManager gwsdManager = GwsdManager.this;
                if (i2 < 2) {
                    gwsdManager.applyGwsdEnabled(gwsdManager.mGwsdTargetEnabled);
                    GwsdManager.this.mGwsdUserSelectRetryTime++;
                    return;
                }
                gwsdManager.logd("Gwsd user select set failed!!!");
                return;
            }
            if (i != 1) {
                if (i == 2 || i == 3 || i == 7) {
                    GwsdManager.this.mGwsdUserSelectRetryTime = 0;
                    GwsdManager.this.mGwsdAutoRejectRetryTime = 0;
                    GwsdManager.this.updateGwsd();
                    return;
                }
                return;
            }
            int i3 = message.arg1;
            GwsdManager gwsdManager2 = GwsdManager.this;
            if (i3 == 0) {
                gwsdManager2.mGwsdAutoRejectRetryTime = 0;
                return;
            }
            int i4 = gwsdManager2.mGwsdUserSelectRetryTime;
            GwsdManager gwsdManager3 = GwsdManager.this;
            if (i4 < 2) {
                gwsdManager3.updateGwsdAutoReject();
                GwsdManager.this.mGwsdAutoRejectRetryTime++;
                return;
            }
            gwsdManager3.logd("Gwsd auto reject set failed!!!");
        }
    };

    public boolean isGwsdAutoRejectMode() {
        return false;
    }

    public boolean isGwsdMode() {
        boolean z = Settings.Secure.getInt(this.mContext.getContentResolver(), "gb_gwsd", 0) == 1;
        logd("isGwsdMode : " + z);
        return z;
    }

    public static GwsdManagerBase make() {
        if (GwsdManagerBase.mInstance instanceof GwsdManager) {
            return GwsdManagerBase.mInstance;
        }
        if (ModemProvider.IS_MTK) {
            GwsdManagerBase.mInstance = new GwsdManager();
        } else if (ModemProvider.IS_QCOM) {
            GwsdManagerBase.mInstance = new GwsdManagerEx();
        }
        return GwsdManagerBase.mInstance;
    }

    private GwsdManager() {
        this.mGwsdContentObserver = null;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(phoneGlobals);
        this.mGwsdContentObserver = new ContentObserver(new Handler(this.mContext.getMainLooper())) { // from class: com.android.phone.GwsdManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                GwsdManager.this.logd("GwsdContent changed!");
                boolean zIsGwsdMode = GwsdManager.this.isGwsdMode();
                GwsdManager gwsdManager = GwsdManager.this;
                if (zIsGwsdMode) {
                    gwsdManager.mExtTelephonyManager.addGwsdListener(GwsdManager.this.mGwsdListenerImpl);
                    MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_gwsd_option");
                } else {
                    gwsdManager.mExtTelephonyManager.removeGwsdListener();
                }
                GwsdManager.this.updateGwsd();
            }
        };
        this.mIsHyperEngineOptSupported = MiuiPhoneUtils.isHyperEngineSupported();
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("gb_gwsd"), false, this.mGwsdContentObserver);
    }

    public class GwsdListenerImpl extends GwsdListener {
        public GwsdListenerImpl() {
        }

        public void onAddListenered(int i, String str) {
            GwsdManager.this.logd("onAddListenered, status: " + i + " reason: " + str);
        }

        public void onUserSelectionModeChanged(int i, String str) {
            GwsdManager.this.logd("onUserSelectionModeChanged, status: " + i + " reason: " + str);
            Message messageObtainMessage = GwsdManager.this.mHandler.obtainMessage(0);
            messageObtainMessage.arg1 = i;
            messageObtainMessage.obj = str;
            GwsdManager.this.mHandler.sendMessage(messageObtainMessage);
        }

        public void onAutoRejectModeChanged(int i, String str) {
            GwsdManager.this.logd("onAutoRejectModeChanged, status: " + i + " reason: " + str);
            Message messageObtainMessage = GwsdManager.this.mHandler.obtainMessage(1);
            messageObtainMessage.arg1 = i;
            GwsdManager.this.mHandler.sendMessage(messageObtainMessage);
        }

        public void onCallValidTimerChanged(int i, String str) {
            GwsdManager.this.logd("onCallValidTimerChanged, status: " + i + " reason: " + str);
            Message messageObtainMessage = GwsdManager.this.mHandler.obtainMessage(5);
            messageObtainMessage.arg1 = i;
            GwsdManager.this.mHandler.sendMessage(messageObtainMessage);
        }

        public void onIgnoreSameNumberIntervalChanged(int i, String str) {
            GwsdManager.this.logd("onIgnoreSameNumberIntervalChanged, status: " + i + " reason: " + str);
            Message messageObtainMessage = GwsdManager.this.mHandler.obtainMessage(6);
            messageObtainMessage.arg1 = i;
            GwsdManager.this.mHandler.sendMessage(messageObtainMessage);
        }

        public void onSyncGwsdInfoFinished(int i, String str) {
            GwsdManager.this.logd("onSyncInfoToServiceFinished, status: " + i + " reason: " + str);
        }

        public void onSystemStateChanged(int i) {
            switch (i) {
                case 7340033:
                    GwsdManager.this.logd("onSystemStateChanged: modem crash");
                    break;
                case 7340034:
                    GwsdManager.this.logd("onSystemStateChanged: default data switched");
                    GwsdManager.this.mHandler.sendMessage(GwsdManager.this.mHandler.obtainMessage(2));
                    break;
                case 7340035:
                    GwsdManager.this.logd("onSystemStateChanged: start default dsda changed");
                    GwsdManager.this.mHandler.sendMessage(GwsdManager.this.mHandler.obtainMessage(3));
                    break;
                case 7340036:
                    GwsdManager.this.logd("onSystemStateChanged: out of service need to restart gwsd");
                    GwsdManager.this.mHandler.sendMessage(GwsdManager.this.mHandler.obtainMessage(7));
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d(LOG_TAG, str);
    }

    private boolean isInHyperEngineGameWhiteList() {
        try {
            List hyperEngineGameWhiteListByCloud = FiveGManagerBase.getHyperEngineGameWhiteListByCloud(this.mContext);
            if (hyperEngineGameWhiteListByCloud != null && !hyperEngineGameWhiteListByCloud.isEmpty()) {
                String topAppName = MiuiPhoneUtils.getTopAppName();
                logd("isInHyperEngineGameWhiteList topGameName=" + topAppName);
                if (TextUtils.isEmpty(topAppName)) {
                    return false;
                }
                return hyperEngineGameWhiteListByCloud.contains(topAppName);
            }
            return false;
        } catch (Exception e) {
            logd("isInHyperEngineGameWhiteList exception: " + e);
            return false;
        }
    }

    public void updateGwsd() {
        boolean zIsGwsdMode = isGwsdMode();
        if (zIsGwsdMode && this.mIsHyperEngineOptSupported) {
            zIsGwsdMode = isInHyperEngineGameWhiteList();
        }
        this.mGwsdTargetEnabled = zIsGwsdMode;
        applyGwsdEnabled(zIsGwsdMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyGwsdEnabled(boolean z) {
        logd("applyGwsdEnabled enabled=" + z);
        int i = 0;
        while (true) {
            int i2 = MiuiPhoneUtils.PHONE_COUNT;
            ExtTelephonyManager extTelephonyManager = this.mExtTelephonyManager;
            if (i < i2) {
                extTelephonyManager.setGwsdEnabled(i, z);
                i++;
            } else {
                extTelephonyManager.setGwsdDualSimEnabled(z);
                return;
            }
        }
    }

    public void updateGwsdAutoReject() {
        this.mExtTelephonyManager.setGwsdAutoRejectEnabled(isGwsdAutoRejectMode());
    }
}
