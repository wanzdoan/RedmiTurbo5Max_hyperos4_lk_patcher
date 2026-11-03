package com.android.phone;

import android.content.Context;
import android.os.Binder;
import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;
import com.android.internal.telephony.HexDump;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.ModemUtils;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class QcMiuiEsimManagerEx extends MiuiEsimManager {
    private static final byte[] SELECT_ISD = {-96, 0, 0, 1, 81, 0, 0, 0};
    private static Context mContext;
    private static ModemUtils mHydra;
    private SEService mSeService;
    private Object serviceMutex = new Object();
    private boolean mIsSeServiceConnected = false;
    private boolean mEsimRecoveryFailSeNotReady = false;
    private Channel mEsimChannel = null;
    private Session mEsimSession = null;

    protected QcMiuiEsimManagerEx() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        ModemUtils modemUtilsInit = ModemUtils.init(phoneGlobals);
        mHydra = modemUtilsInit;
        if (modemUtilsInit != null) {
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", new BaseEvent.IEventListener() { // from class: com.android.phone.QcMiuiEsimManagerEx$$ExternalSyntheticLambda0
                @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
                public final void onEvent(BaseEvent baseEvent) {
                    this.f$0.lambda$new$0(baseEvent);
                }
            });
        }
        MiuiEsimManager.isCanRestUimPowerForOpenChannelFile = true;
        this.mSeService = new SEService(mContext.getApplicationContext(), Executors.newFixedThreadPool(1), new SEService.OnConnectedListener() { // from class: com.android.phone.QcMiuiEsimManagerEx.1
            @Override // android.se.omapi.SEService.OnConnectedListener
            public void onConnected() {
                synchronized (QcMiuiEsimManagerEx.this.serviceMutex) {
                    try {
                        MiuiEsimManagerBase.logi("seService is connected, mEsimRecoveryFailSeNotReady = " + QcMiuiEsimManagerEx.this.mEsimRecoveryFailSeNotReady);
                        QcMiuiEsimManagerEx.this.mIsSeServiceConnected = true;
                        if (QcMiuiEsimManagerEx.this.mEsimRecoveryFailSeNotReady) {
                            MiuiEsimManagerBase.mHandler.sendEmptyMessage(12);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
            synchronized (this.serviceMutex) {
                try {
                    MiuiEsimManagerBase.logi("qcrilhookReady");
                    if (this.mIsSeServiceConnected) {
                        MiuiEsimManagerBase.mHandler.sendEmptyMessage(12);
                    } else {
                        this.mEsimRecoveryFailSeNotReady = true;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public boolean isEsimActive() {
        return super.isEsimActive();
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public boolean isSupportEsimForCountry(String str) {
        return super.isSupportEsimForCountry(str);
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public int setEsimState(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (i == 1) {
            try {
                if (MiuiEsimManagerBase.isRemoveEsimSwitch()) {
                    return 0;
                }
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        if (!stopVsimIfNeeded(i)) {
            MiuiEsimManagerBase.logd("stopVsimIfNeeded failed, state = " + i);
            return -1;
        }
        int esimGPIOState = getEsimGPIOState();
        if ((i == 0 && esimGPIOState == 0) || (i == 1 && esimGPIOState == 1)) {
            MiuiEsimManagerBase.logi("Ignore duplicate logic, state = " + i + ", currentGPIO = " + esimGPIOState);
            return 0;
        }
        int i2 = i == 0 ? 1 : 0;
        int i3 = i == 0 ? -5 : -6;
        int i4 = i == 0 ? -7 : -8;
        if (!openChannel()) {
            return -9;
        }
        int i5 = i == 0 ? 0 : 1;
        int i6 = i == 0 ? 1 : 0;
        if (!onUimPowerRequest(0, -1, i5) && !retrySetEsimState(i3, i)) {
            closeChannel();
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i3));
            return i3;
        }
        if (!onUimPowerRequest(1, i2, i6) && !retrySetEsimState(i4, i)) {
            closeChannel();
            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i4));
            return i4;
        }
        MiuiEsimManagerBase.setEsimSecureValue(i);
        StringBuilder sb = new StringBuilder("setEsimState for ");
        sb.append(i == 0 ? "enable_esim" : "disable_esim");
        sb.append(" result = ");
        sb.append(0);
        MiuiEsimManagerBase.logi(sb.toString());
        closeChannel();
        return 0;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public int getEsimGPIOState() {
        return getEfsState() == 1 ? 0 : 1;
    }

    public int getEfsState() {
        byte[] efs;
        ModemUtils modemUtils = mHydra;
        byte b = (modemUtils == null || (efs = modemUtils.readEfs(1, "/nv/item_files/modem/uim/uimdrv/esim_enable")) == null || efs.length <= 0) ? (byte) -4 : efs[0];
        if (b != 0 && b != 1) {
            MiuiEsimManagerBase.trackerEsimEvent("getEfsState", Integer.valueOf(b));
        }
        MiuiEsimManagerBase.logd("getEfsState state == " + ((int) b));
        return b;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public String getProductEid() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = mHydra;
        String hexString = "";
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookEfsOptSync", 1, "/nv/item_files/modem/uim/uimdrv/esim_eid", 4);
            if (objCallHook instanceof ByteBuffer) {
                byteBuffer = (ByteBuffer) objCallHook;
            } else {
                MiuiEsimManagerBase.loge("getProductEid: return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                byteBuffer = null;
            }
            if (byteBuffer == null) {
                MiuiEsimManagerBase.logi("getProductEid read eid efs failed");
                return "";
            }
            if (byteBuffer.getInt() == 0) {
                byteBuffer.getInt();
                int iRemaining = byteBuffer.remaining();
                byte[] bArr = new byte[iRemaining];
                byteBuffer.get(bArr, 0, iRemaining);
                hexString = HexDump.toHexString(bArr);
            }
        }
        MiuiEsimManagerBase.logi("getProductEid final eid = " + TelephonyUtils.pii(hexString));
        return hexString;
    }

    @Override // com.android.phone.MiuiEsimManager, com.android.phone.MiuiEsimManagerBase
    public void writeProductEid(byte[] bArr) {
        boolean zBooleanValue;
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookEfsWrite", 1, "/nv/item_files/modem/uim/uimdrv/esim_eid", bArr);
            if (objCallHook instanceof Boolean) {
                zBooleanValue = ((Boolean) objCallHook).booleanValue();
            } else {
                MiuiEsimManagerBase.loge("writeProductEid: return type not Boolean, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                zBooleanValue = false;
            }
        } else {
            zBooleanValue = false;
        }
        MiuiEsimManagerBase.trackerEsimEvent("writeProductEid", Boolean.valueOf(zBooleanValue));
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public void restUimPowerState(int i) {
        MiuiEsimManagerBase.logi("restUimPowerState  reason = " + i);
        MiuiEsimManagerBase.trackerEsimEvent("restUimPowerState", Integer.valueOf(i));
        if (i != 1001) {
            if (i != 1002) {
                return;
            }
            restUimPowerState();
        } else if (MiuiEsimManager.isCanRestUimPowerForOpenChannelFile && MiuiEsimManagerBase.getEsimSecureValue() == 1) {
            restUimPowerState();
            MiuiEsimManager.isCanRestUimPowerForOpenChannelFile = false;
        }
    }

    private void restUimPowerState() {
        int esimGPIOState = getEsimGPIOState();
        int i = esimGPIOState == 0 ? 1 : 0;
        MiuiEsimManagerBase.logi("restUimPowerState currentGPIO = " + esimGPIOState);
        if (!onUimPowerRequest(0, -1, i)) {
            boolean zOnUimPowerRequest = false;
            for (int i2 = 0; i2 < 5; i2++) {
                zOnUimPowerRequest = onUimPowerRequest(0, -1, i);
                if (zOnUimPowerRequest) {
                    break;
                }
            }
            if (!zOnUimPowerRequest) {
                MiuiEsimManagerBase.logi("restUimPowerState UIM_POWER_DOWN fail...");
            }
        }
        if (onUimPowerRequest(1, 1, i)) {
            return;
        }
        boolean zOnUimPowerRequest2 = false;
        for (int i3 = 0; i3 < 5; i3++) {
            zOnUimPowerRequest2 = onUimPowerRequest(1, 1, i);
            if (zOnUimPowerRequest2) {
                break;
            }
        }
        if (zOnUimPowerRequest2) {
            return;
        }
        MiuiEsimManagerBase.logi("restUimPowerState UIM_POWER_UP fail...");
    }

    private static boolean onUimPowerRequest(int i, int i2, int i3) {
        MiuiEsimManagerBase.logi("onUimPowerRequest, mHydra = " + mHydra);
        ModemUtils modemUtils = mHydra;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookEsimPowerReqEx", Integer.valueOf(i), 2, Integer.valueOf(i2), Integer.valueOf(i3));
            MiuiEsimManagerBase.logi("onUimPowerRequest, callResult = " + objCallHook);
            if (objCallHook instanceof Boolean) {
                return ((Boolean) objCallHook).booleanValue();
            }
            MiuiEsimManagerBase.loge("onUimPowerRequest: return type not Boolean, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        }
        return false;
    }

    private static boolean retrySetEsimState(int i, int i2) {
        StringBuilder sb = new StringBuilder("retrySetEsimState ");
        sb.append(i2 == 0 ? "enable_esim" : "disable_esim");
        sb.append(" errCode = ");
        sb.append(i);
        MiuiEsimManagerBase.logi(sb.toString());
        int i3 = i2 == 0 ? 0 : 1;
        int i4 = i2 == 0 ? 1 : 0;
        if (i != -8 && i != -7) {
            if (i != -6 && i != -5) {
                return false;
            }
            for (int i5 = 0; i5 < 5; i5++) {
                if (onUimPowerRequest(0, -1, i3)) {
                    return true;
                }
            }
            return false;
        }
        int i6 = i2 == 0 ? 1 : 0;
        boolean zOnUimPowerRequest = false;
        for (int i7 = 0; i7 < 5; i7++) {
            if (i7 < 4) {
                zOnUimPowerRequest = onUimPowerRequest(1, i6, i4);
            } else {
                onUimPowerRequest(0, -1, i3);
                onUimPowerRequest(1, i6, i4);
            }
            if (zOnUimPowerRequest) {
                MiuiEsimManagerBase.setEsimSecureValue(i2);
                return true;
            }
        }
        return false;
    }

    private boolean openChannel() {
        closeChannel();
        this.mEsimSession = null;
        try {
            for (Reader reader : this.mSeService.getReaders()) {
                if (reader.getName().startsWith("eSE")) {
                    this.mEsimSession = reader.openSession();
                    break;
                }
            }
            if (this.mEsimSession == null) {
                MiuiEsimManagerBase.logi("wake up ese and session is null");
                return false;
            }
            MiuiEsimManagerBase.logi("openLogicalChannel");
            this.mEsimChannel = this.mEsimSession.openLogicalChannel(SELECT_ISD);
            MiuiEsimManagerBase.logi("mEsimChannel = " + this.mEsimChannel);
            if (this.mEsimChannel == null) {
                MiuiEsimManagerBase.logi("open esim channel failed");
                this.mEsimSession.close();
                this.mEsimSession = null;
                return false;
            }
            MiuiEsimManagerBase.logi("open esim channel sucess");
            return true;
        } catch (Exception e) {
            closeChannel();
            MiuiEsimManagerBase.logi("Unexpected Exception " + e);
            return false;
        }
    }

    private void closeChannel() {
        MiuiEsimManagerBase.logi("closeChannel mEsimChannel = " + this.mEsimChannel);
        if (this.mEsimChannel != null) {
            MiuiEsimManagerBase.logi("close channel");
            this.mEsimChannel.close();
            this.mEsimChannel = null;
        }
        if (this.mEsimSession != null) {
            MiuiEsimManagerBase.logi("close session");
            this.mEsimSession.close();
            this.mEsimSession = null;
        }
    }
}
