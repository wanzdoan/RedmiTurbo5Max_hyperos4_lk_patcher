package com.android.phone.beidou;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.VideoCodecCapability;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.xiaomi.bdsms.IBDSMSCallback;
import com.xiaomi.bdsms.IBDSMSservice;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BdConnectionService {
    public static final long DELAY_RETRY_SHORT_TIMER = 500;
    private static volatile BdConnectionService INSTANCE;
    private IBDSMSservice mIBDSMSService;
    private HashSet mBdConnectionStatusListeners = new HashSet();
    private final ServiceConnection mBDSMSServiceConnection = new ServiceConnection() { // from class: com.android.phone.beidou.BdConnectionService.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BdConnectionService.this.mIBDSMSService = IBDSMSservice.Stub.asInterface(iBinder);
            BdConnectionService.this.notifyServiceConnected();
            BdConnectionService.logi("BdServiceConnected");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            BdConnectionService.this.mIBDSMSService = null;
            BdConnectionService.this.notifyServiceDisconnected();
            BdConnectionService.logi("BdServiceDisconnected");
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    public interface BdConnectionStatusListener {
        void serviceConnected();

        void serviceDisconnected();
    }

    private BdConnectionService() {
    }

    public static void init() {
        if (INSTANCE == null) {
            synchronized (BdSmsController.class) {
                try {
                    if (INSTANCE == null) {
                        INSTANCE = new BdConnectionService();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static BdConnectionService getInstance() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }

    public void initService() {
        logd("initService ： mIBDSMSService = " + this.mIBDSMSService);
        if (this.mIBDSMSService == null) {
            bindBdSmsService();
        }
    }

    public void closeService() {
        logd("closeBdSmsService ： mIBDSMSService = " + this.mIBDSMSService);
        if (this.mIBDSMSService != null) {
            try {
                this.mContext.unbindService(this.mBDSMSServiceConnection);
            } catch (Exception unused) {
                loge("closeService ： unbindService fail");
            }
            this.mIBDSMSService = null;
        }
    }

    private void bindBdSmsService() {
        Intent intent = new Intent("com.xiaomi.bdsms.BDSMSService");
        intent.setPackage("com.xiaomi.bdsms");
        intent.setClassName("com.xiaomi.bdsms", "com.xiaomi.bdsms.BDSMSService");
        this.mContext.bindService(intent, this.mBDSMSServiceConnection, 577);
    }

    public boolean getBdServiceStatus(int i) {
        int iQueryServiceStatus;
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            iQueryServiceStatus = iBDSMSservice != null ? iBDSMSservice.queryServiceStatus(i) : 0;
        } catch (Exception e) {
            loge("getBdServiceStatus failed : " + e);
        }
        return iQueryServiceStatus == 2;
    }

    public int sendBDMessageWithLocation(int i, String str, String str2, int i2, List list, double d, double d2) {
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice != null) {
                return iBDSMSservice.sendBDMessageWithLocation(i, str, str2, i2, list, d, d2);
            }
            return -1;
        } catch (Exception e) {
            loge("sendBDMessageWithLocation failed : " + e);
            return -1;
        }
    }

    public int sendBDMessage(int i, String str, String str2, int i2, List list) {
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice != null) {
                return iBDSMSservice.sendBDMessage(i, str, str2, i2, list);
            }
            return -1;
        } catch (Exception e) {
            loge("sendBDMessage failed : " + e);
            return -1;
        }
    }

    public int sendBDAudioMessage(int i, Uri uri, Uri uri2, String str, int i2, List list) {
        try {
            if (this.mIBDSMSService == null) {
                return -1;
            }
            logd("sendBDAudioMessage");
            return this.mIBDSMSService.sendBDAudio(i, uri, uri2, str, i2, list);
        } catch (Exception e) {
            loge("sendBDAudioMessage failed : " + e);
            return -1;
        }
    }

    public int sendBDAudioMessageWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2) {
        try {
            if (this.mIBDSMSService == null) {
                return -1;
            }
            logd("sendBDAudioMessageWithLocation");
            return this.mIBDSMSService.sendBDAudioWithLocation(i, uri, uri2, str, i2, list, d, d2);
        } catch (Exception e) {
            loge("sendBDAudioMessageWithLocation failed : " + e);
            return -1;
        }
    }

    public int sendBDPictureMessage(int i, Uri uri, Uri uri2, String str, int i2, List list) {
        try {
            if (this.mIBDSMSService == null) {
                return -1;
            }
            logd("sendBDPictureMessage");
            return this.mIBDSMSService.sendBDPicture(i, uri, uri2, str, i2, list);
        } catch (Exception e) {
            loge("sendBDPictureMessage failed : " + e);
            return -1;
        }
    }

    public int sendBDPictureMessageWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2) {
        try {
            if (this.mIBDSMSService == null) {
                return -1;
            }
            logd("sendBDPictureMessageWithLocation");
            return this.mIBDSMSService.sendBDPictureWithLocation(i, uri, uri2, str, i2, list, d, d2);
        } catch (Exception e) {
            loge("sendBDPictureMessageWithLocation failed : " + e);
            return -1;
        }
    }

    public int receiveBDMessage(String str, int i) {
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice != null) {
                return iBDSMSservice.receiveBDMessage(str, i);
            }
            return -1;
        } catch (Exception e) {
            loge("receiveBDMessage failed : " + e);
            return -1;
        }
    }

    public boolean isSupportedBDPicture() {
        boolean z = VideoCodecCapability.getParameters("Image_AI_Encoder") == 1;
        Log.d("BdConnectionService", "isSupportedBDPicture = " + z);
        return z;
    }

    public boolean isSupportedBDAudio() {
        boolean z = SystemProperties.getBoolean("vendor.audio.support.aihc.codec", false);
        Log.d("BdConnectionService", "isSupportedBDAudio = " + z);
        return z;
    }

    public void interruptSendingFlow() {
        try {
            if (this.mIBDSMSService != null) {
                logd("interruptSendingFlow");
                this.mIBDSMSService.interruptSendingFlow();
            }
        } catch (Exception e) {
            loge("interruptSendingFlow failed : " + e);
        }
    }

    public int queryRemainMsgCount(int i) {
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice != null) {
                return iBDSMSservice.queryRemainMsgCount(i);
            }
            return -1;
        } catch (Exception e) {
            loge("getRemainBDSmsCount failed : " + e);
            return -1;
        }
    }

    public int queryAllowedSendTime() {
        int iQueryAllowedSendTime = 0;
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice == null) {
                return 0;
            }
            iQueryAllowedSendTime = iBDSMSservice.queryAllowedSendTime();
            logd("queryAllowedSendTime : allowedSendTime = " + iQueryAllowedSendTime);
            return iQueryAllowedSendTime;
        } catch (Exception e) {
            loge("queryAllowedSendTime failed : " + e);
            return iQueryAllowedSendTime;
        }
    }

    public int queryAllowedReceiveTime() {
        int iQueryAllowedReceiveTime = 0;
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice == null) {
                return 0;
            }
            iQueryAllowedReceiveTime = iBDSMSservice.queryAllowedReceiveTime();
            logd("queryAllowedReceiveTime : allowedReceiveTime = " + iQueryAllowedReceiveTime);
            return iQueryAllowedReceiveTime;
        } catch (Exception e) {
            loge("queryAllowedReceiveTime failed : " + e);
            return iQueryAllowedReceiveTime;
        }
    }

    public void syncBDMail(int i) {
        try {
            if (this.mIBDSMSService != null) {
                logd("syncBDMail : slotId = " + i);
                this.mIBDSMSService.syncBDMail(i);
            }
        } catch (Exception e) {
            loge("syncBDMail failed : " + e);
        }
    }

    public void updateAuthKey(int i) {
        try {
            if (this.mIBDSMSService != null) {
                logd("updateAuthKey : slotId = " + i);
                this.mIBDSMSService.updateAuthKey(i);
            }
        } catch (RemoteException | NullPointerException e) {
            loge("updateAuthKey failed : " + e);
        }
    }

    public void updateEphData(int i) {
        try {
            if (this.mIBDSMSService != null) {
                logd("updateEphData : slotId = " + i);
                this.mIBDSMSService.updateEphData(i);
            }
        } catch (RemoteException | NullPointerException e) {
            loge("updateEphData failed : " + e);
        }
    }

    public boolean checkEphData() {
        int iCheckEphData;
        try {
            IBDSMSservice iBDSMSservice = this.mIBDSMSService;
            if (iBDSMSservice != null) {
                iCheckEphData = iBDSMSservice.checkEphData();
                try {
                    logd("checkEphData : state = " + iCheckEphData);
                } catch (Exception e) {
                    e = e;
                    loge("checkEphData failed : " + e);
                }
            } else {
                iCheckEphData = 1;
            }
        } catch (Exception e2) {
            e = e2;
            iCheckEphData = 1;
        }
        return iCheckEphData == 2;
    }

    public int preLoad(int i) {
        IBDSMSservice iBDSMSservice = this.mIBDSMSService;
        if (iBDSMSservice == null) {
            return 1;
        }
        try {
            return iBDSMSservice.preLoad(i);
        } catch (Exception e) {
            loge("preLoad failed : " + e);
            return 1;
        }
    }

    public boolean isBdSmsServiceReady() {
        return this.mIBDSMSService != null;
    }

    public void setCallBack(IBDSMSCallback iBDSMSCallback) {
        try {
            this.mIBDSMSService.SetCallback(iBDSMSCallback);
            logd("setCallBack");
        } catch (Exception e) {
            loge("setCallBack failed : " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyServiceConnected() {
        Iterator it = this.mBdConnectionStatusListeners.iterator();
        while (it.hasNext()) {
            ((BdConnectionStatusListener) it.next()).serviceConnected();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyServiceDisconnected() {
        Iterator it = this.mBdConnectionStatusListeners.iterator();
        while (it.hasNext()) {
            ((BdConnectionStatusListener) it.next()).serviceDisconnected();
        }
    }

    public void registerBdConnectionStatusListener(BdConnectionStatusListener bdConnectionStatusListener) {
        if (bdConnectionStatusListener == null) {
            return;
        }
        this.mBdConnectionStatusListeners.add(bdConnectionStatusListener);
    }

    protected static void logi(String str) {
        Log.i("BdConnectionService", str);
    }

    protected static void loge(String str) {
        Log.e("BdConnectionService", str);
    }

    protected static void logd(String str) {
        Log.d("BdConnectionService", str);
    }
}
