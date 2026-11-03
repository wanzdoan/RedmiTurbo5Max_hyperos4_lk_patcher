package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.service.euicc.EuiccProfileInfo;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.euicc.EuiccPort;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class EsimManager {
    private static String LOG_TAG = "EsimManager";
    protected static Context mContext;
    protected static EsimManager sInstance;
    private EuiccManager mEuiccManager;
    private ESimHandler mHandler;
    private boolean isReceiverBootCompleted = false;
    private boolean isSimPresent = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: miui.telephony.EsimManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            EsimManager.logi("receive action : " + action);
            if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action)) {
                int simState = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                int phoneId = intent.getIntExtra("phone", -1);
                EsimManager.logi("receive simState : " + simState + ", phoneId =" + phoneId);
                if (simState == 11 && phoneId == 1) {
                    EsimManager.this.isSimPresent = true;
                    EsimManager.this.updateDownLoadProfileFlags();
                }
            }
            if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                EsimManager.this.isReceiverBootCompleted = true;
                EsimManager.this.updateDownLoadProfileFlags();
            }
        }
    };

    private static class ResultCaptor<T> extends AsyncResultCallback<T> {
        public Throwable exception;
        private CountDownLatch mLatch;
        final long mWaitTime;
        public T result;

        private ResultCaptor() {
            this.mWaitTime = 5000L;
            this.mLatch = new CountDownLatch(1);
        }

        public void onResult(T r) {
            EsimManager.logi("onResult E ");
            this.result = r;
            this.mLatch.countDown();
        }

        public void onException(Throwable e) {
            EsimManager.logi("onException : " + e);
            this.exception = e;
            this.mLatch.countDown();
        }

        public T retrieveResult() {
            try {
                EsimManager.logi("mLatch.await start");
                CountDownLatch countDownLatch = this.mLatch;
                Objects.requireNonNull(this);
                countDownLatch.await(5000L, TimeUnit.MILLISECONDS);
                EsimManager.logi("mLatch.await end");
                return this.result;
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                return this.result;
            }
        }
    }

    protected EsimManager(Context context) {
        logi("EsimManager init.");
        mContext = context;
        this.mEuiccManager = (EuiccManager) mContext.getSystemService("euicc");
        HandlerThread mHandlerThread = new HandlerThread("EsimManager");
        mHandlerThread.start();
        this.mHandler = new ESimHandler(this, mHandlerThread.getLooper());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        context.registerReceiver(this.mReceiver, filter, 2);
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new EsimManager(context);
        }
    }

    public static EsimManager getInstance() {
        return sInstance;
    }

    public boolean isEsimActive() {
        boolean isEsimActive = false;
        String eid = getEidFromSlotId(1);
        if (this.mEuiccManager != null && this.mEuiccManager.isEnabled() && eid != null) {
            UiccSlot[] slots = UiccController.getInstance().getUiccSlots();
            for (int i = 0; i < slots.length; i++) {
                UiccSlot uiccSlot = slots[i];
                String iccid = uiccSlot.getIccId(uiccSlot.getPortIndexFromPhoneId(i));
                logi("slots =" + i + ",iccid = " + TelephonyUtils.pii(iccid));
                if (uiccSlot.isEuicc() && isValidIccid(iccid)) {
                    isEsimActive = true;
                }
            }
        }
        logi("isEsimActive =" + isEsimActive);
        return isEsimActive;
    }

    public boolean isValidIccid(String iccid) {
        if (TextUtils.isEmpty(iccid)) {
            return false;
        }
        return !TextUtils.isEmpty(iccid.replaceAll("(?i)f*$", ""));
    }

    public String getEidFromSlotId(int slotId) {
        long identity = Binder.clearCallingIdentity();
        String eid = null;
        try {
            UiccSlotInfo[] slotInfos = TelephonyManager.from(mContext).getUiccSlotsInfo();
            if (slotInfos != null && slotId < slotInfos.length && slotInfos[slotId] != null) {
                String cardId = slotInfos[slotId].getCardId();
                logi("getEidFromSlotId cardId=" + TelephonyUtils.pii(cardId));
                if (slotInfos[slotId].getIsEuicc() && cardId != null && !cardId.isEmpty() && cardId.length() == 32) {
                    eid = cardId;
                }
            }
            logi("getEidFromSlotId eid=" + TelephonyUtils.pii(eid) + ",slotId=" + slotId);
            return eid;
        } catch (Exception e) {
            logi("getEidFromSlotId exception= " + e);
            return null;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isSupportEsim() {
        String eid = getEidFromSlotId(1);
        if (this.mEuiccManager == null || !this.mEuiccManager.isEnabled() || eid == null) {
            return false;
        }
        return true;
    }

    protected class ESimHandler extends Handler {
        public ESimHandler(EsimManager this$0, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            EsimManager.logi("eSimHandler handleMessage what=" + msg.what);
            int i = msg.what;
        }
    }

    public void getAllProfiles(String cardId) {
        logi("EuiccPort getAllProfiles begin : " + cardId);
        EuiccPort euiccPort = getEuiccPort(cardId);
        if (euiccPort == null) {
            logi("euiccPort is null");
            return;
        }
        ResultCaptor<EuiccProfileInfo[]> resultCaptor = new ResultCaptor<>();
        euiccPort.getAllProfiles(resultCaptor, this.mHandler);
        EuiccProfileInfo[] profiles = resultCaptor.retrieveResult();
        for (EuiccProfileInfo profile : profiles) {
            logi("EuiccProfileInfo profile=" + profile);
        }
        if (profiles.length > 0) {
            Settings.Global.putInt(mContext.getContentResolver(), "euicc_provisioned", 1);
        }
    }

    private EuiccPort getEuiccPort(String cardId) {
        UiccSlot slot;
        EuiccPort uiccPort;
        UiccController controller = UiccController.getInstance();
        int slotId = controller.getUiccSlotForCardId(cardId);
        if (slotId != -1 && (slot = controller.getUiccSlot(slotId)) != null && slot.isEuicc() && (uiccPort = controller.getUiccPort(slotId)) != null && (uiccPort instanceof EuiccPort)) {
            return uiccPort;
        }
        logi("EuiccPort is null. CardId : " + TelephonyUtils.pii(cardId));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDownLoadProfileFlags() {
        logi("updateDownLoadProfileFlags E");
        if (this.isSimPresent && this.isReceiverBootCompleted) {
            new Thread(new Runnable() { // from class: miui.telephony.EsimManager.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        String cardId = EsimManager.this.getEidFromSlotId(1);
                        if (cardId != null) {
                            EsimManager.this.getAllProfiles(cardId);
                        }
                        EsimManager.this.mHandler.getLooper().quit();
                        EsimManager.this.mHandler = null;
                    } catch (Exception e) {
                        EsimManager.logi("getAllProfiles failed" + e);
                    }
                }
            }).start();
            this.isSimPresent = false;
            this.isReceiverBootCompleted = false;
        }
    }

    protected static void log(String msg) {
        Log.d(LOG_TAG, msg);
    }

    protected static void logi(String msg) {
        Log.i(LOG_TAG, msg);
    }
}
