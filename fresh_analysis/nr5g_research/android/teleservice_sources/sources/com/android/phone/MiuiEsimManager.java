package com.android.phone;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceManager;
import android.service.euicc.EuiccProfileInfo;
import android.telephony.UiccSlotMapping;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.asn1.Asn1Decoder;
import com.android.internal.telephony.uicc.asn1.Asn1Node;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.Utils;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiEsimManager extends MiuiEsimManagerBase {
    public static boolean isCanRestUimPowerForOpenChannelFile = false;
    private static int mChannelId;
    private static String[] mCloseChannelResult;
    private static String mCurrentSVN;
    private static String mEID;
    private static int mESimState;
    private static String[] mOpenChannelResult;
    private static String[] mSendAPDUResult;
    private static int mSetESimStateResult;
    private static final byte[] DOWNGRADE_VERSION_COMMAND = {-65, -126, 3, 2, 3, 0};
    private static final byte[] UPGRADE_VERSION_COMMAND = {-65, -126, 3, 2, 5, 0};
    private static final byte[] GET_INFO1_COMMAND = {-65, 32, 0};
    private static final byte[] GET_INFO2_COMMAND = {-65, 32, 0};

    public static MiuiEsimManagerBase make() {
        if (MiuiEsimManagerBase.sInstance == null) {
            if (ModemProvider.IS_QCOM) {
                if (MiuiPhoneUtils.shouldWakeUpESE()) {
                    MiuiEsimManagerBase.sInstance = new QcMiuiEsimManagerEx();
                } else {
                    MiuiEsimManagerBase.sInstance = new QcMiuiEsimManager();
                }
            } else {
                MiuiEsimManagerBase.sInstance = new MiuiEsimManager();
                MiuiEsimManagerBase.logi("make MiuiEsimManager");
            }
        }
        return MiuiEsimManagerBase.sInstance;
    }

    protected MiuiEsimManager() {
        if (ModemProvider.IS_QCOM) {
            return;
        }
        recoveryForInit();
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public boolean isEsimActive() {
        return super.isEsimActive();
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public boolean isSupportEsimForCountry(String str) {
        return super.isSupportEsimForCountry(str);
    }

    private int getEsimTimeout() {
        return HyperOSCustFeatureResolve.getInt("cust_waiting_time_when_setting_esim", 3000);
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public int setEsimState(final int i) {
        int i2;
        int euiccBindingResult;
        if (ModemProvider.IS_QCOM) {
            return -1;
        }
        MiuiEsimManagerBase.logi("setEsimState start...");
        if (i == 0 && MiuiPhoneUtils.isSupportEuiccBinding() && ExtTelephonyManager.getInstance(MiuiEsimManagerBase.mContext).getEuiccBindingStatus() != this.STATUS_ALREADY_BOUND && (euiccBindingResult = getEuiccBindingResult()) != -2 && euiccBindingResult != 0) {
            MiuiEsimManagerBase.loge("euicc binding status abnormal, not allowed setEsimState.");
            MiuiEsimManagerBase.trackerEsimEvent("enable_esim", Integer.valueOf(euiccBindingResult + 100));
            return 6;
        }
        if (i == 1 && MiuiEsimManagerBase.isRemoveEsimSwitch()) {
            return 0;
        }
        if (!stopVsimIfNeeded(i)) {
            MiuiEsimManagerBase.logd("stopVsimIfNeeded failed, state = " + i);
            return -1;
        }
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (MiuiPhoneUtils.isSupportMep() && i == 1) {
            MiuiEsimManagerBase.disableProfile();
            if (isLui(phoneGlobals.getPackageManager())) {
                MiuiEsimManagerBase.logi("disable eSIM switch for LUI products, disableEnabledEsimProfiles");
                disableEnabledEsimProfiles();
            }
            MiuiEsimManagerBase.switchToRemovableSlot();
        }
        synchronized (MiuiEsimManagerBase.mLock) {
            if (MiuiEsimManagerBase.isNeedSwitchMapping) {
                try {
                    MiuiEsimManagerBase.mLock.wait(2000L);
                    MiuiEsimManagerBase.mIsSwitchingSlotMapping = false;
                    MiuiEsimManagerBase.isNeedSwitchMapping = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final int i3 = i == 0 ? 1 : 0;
        mSetESimStateResult = 4095;
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("setEsimState");
            handlerThread.start();
            Message messageObtainMessage = new Handler(handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult != null && asyncResult.exception == null) {
                        try {
                            String[] strArr = (String[]) asyncResult.result;
                            if (strArr != null && strArr.length == 1) {
                                String[] strArrSplit = strArr[0].split(",");
                                MiuiEsimManagerBase.logi("setEsimState ActualResult = " + Arrays.toString(strArrSplit) + " , data length = " + strArrSplit.length + " , Integer.parseInt(data[0]) = " + Integer.parseInt(strArrSplit[0]));
                                int i4 = Integer.parseInt(strArrSplit[0]);
                                switch (i4) {
                                    case -3:
                                    case -2:
                                    case -1:
                                    case 2:
                                    case 3:
                                        MiuiEsimManager.mSetESimStateResult = -1;
                                        MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i4));
                                        break;
                                    case 0:
                                    case 1:
                                        MiuiEsimManager.mSetESimStateResult = 0;
                                        break;
                                    case 4:
                                    case 5:
                                        MiuiEsimManager.mSetESimStateResult = Integer.parseInt(strArrSplit[0]);
                                        MiuiEsimManager.this.retrySetEsimState(i3);
                                        break;
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    getLooper().quit();
                }
            }.obtainMessage();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_ESIM_STATE", i3 + ""}, messageObtainMessage);
                handlerThread.join((long) getEsimTimeout());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (i3 == 1 && mSetESimStateResult == 4095 && "com.android.provision".equals(MiuiPhoneUtils.getTopAppName())) {
            MiuiEsimManagerBase.logi("setEsimState for enable eSIM during provisioning process");
            MiuiEsimManagerBase.setEsimSecureValue(0);
            return 0;
        }
        if (MiuiPhoneUtils.isSupportMep() && (4 == (i2 = mSetESimStateResult) || 5 == i2)) {
            synchronized (MiuiEsimManagerBase.mLock) {
                try {
                    MiuiEsimManagerBase.mLock.wait(MirrorDesktopHelper.TIMEOUT_MILLIS);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        if (mSetESimStateResult == 0) {
            MiuiEsimManagerBase.setEsimSecureValue(i);
            if (i == 0 && MiuiEsimManagerBase.isNeedDownGradeSVNDevice()) {
                MiuiEsimManagerBase.ESimHandler eSimHandler = MiuiEsimManagerBase.mHandler;
                eSimHandler.sendMessageDelayed(eSimHandler.obtainMessage(11), 3000L);
            }
        } else if (MiuiPhoneUtils.isSupportMep() && i == 1) {
            MiuiEsimManagerBase.switchSlotMappingForCloseEsimFail();
        }
        StringBuilder sb = new StringBuilder("setEsimState for ");
        sb.append(i3 == 1 ? "enable" : "disable");
        sb.append(" eSIM  result = ");
        sb.append(mSetESimStateResult);
        MiuiEsimManagerBase.logi(sb.toString());
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(phoneGlobals);
        String string = defaultSharedPreferences != null ? defaultSharedPreferences.getString("osu_file_name", null) : null;
        if (i == 0 && mSetESimStateResult == 0 && string == null) {
            MiuiOsuManager.updateOsuFileNamePreference();
        }
        return mSetESimStateResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrySetEsimState(final int i) {
        if (MiuiPhoneUtils.isSupportMep()) {
            try {
                MiuiEsimManagerBase.mTelMgr.setSimSlotMapping(Arrays.asList(new UiccSlotMapping(0, 0, 0), new UiccSlotMapping(0, 1, 1)));
            } catch (Exception e) {
                MiuiEsimManagerBase.loge("retrySetEsimState setSimSlotMapping fail = " + e);
            }
            synchronized (MiuiEsimManagerBase.mLock) {
                try {
                    MiuiEsimManagerBase.mLock.wait(1000L);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            Phone phone = MiuiPhoneUtils.getPhone(1);
            if (phone != null) {
                HandlerThread handlerThread = new HandlerThread("retrySetEsimState");
                handlerThread.start();
                Message messageObtainMessage = new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.2
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                String[] strArr = (String[]) asyncResult.result;
                                if (strArr != null && strArr.length == 1) {
                                    int i2 = Integer.parseInt(strArr[0].split(",")[0]);
                                    switch (i2) {
                                        case -3:
                                        case -2:
                                        case -1:
                                        case 2:
                                        case 3:
                                        case 4:
                                        case 5:
                                            MiuiEsimManager.mSetESimStateResult = -1;
                                            MiuiEsimManagerBase.trackerEsimEvent(i == 0 ? "enable_esim" : "disable_esim", Integer.valueOf(i2));
                                            break;
                                        case 0:
                                        case 1:
                                            MiuiEsimManager.mSetESimStateResult = 0;
                                            break;
                                    }
                                }
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage();
                try {
                    ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_ESIM_STATE", i + ""}, messageObtainMessage);
                    handlerThread.join((long) getEsimTimeout());
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            synchronized (MiuiEsimManagerBase.mLock) {
                try {
                    MiuiEsimManagerBase.mLock.notifyAll();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public int getEsimGPIOState() {
        if (ModemProvider.IS_QCOM) {
            return -1;
        }
        mESimState = 4095;
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("getESimGPIOState");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_GET_ESIM_STATE"}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.3
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                String[] strArr = (String[]) asyncResult.result;
                                if (strArr != null && strArr.length == 1) {
                                    String[] strArrSplit = strArr[0].split(",");
                                    MiuiEsimManagerBase.logd("getEsimGPIOState ActualResult = " + Arrays.toString(strArrSplit) + " , data length = " + strArrSplit.length + " , Integer.parseInt(data[0]) = " + Integer.parseInt(strArrSplit[0]));
                                    int i = Integer.parseInt(strArrSplit[0]);
                                    if (i == -3 || i == -2 || i == -1) {
                                        MiuiEsimManager.mESimState = Integer.parseInt(strArrSplit[0]);
                                        MiuiEsimManagerBase.trackerEsimEvent("getEsimState", Integer.valueOf(MiuiEsimManager.mESimState));
                                    } else if (i == 0) {
                                        MiuiEsimManager.mESimState = 1;
                                    } else if (i == 1) {
                                        MiuiEsimManager.mESimState = 0;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MiuiEsimManagerBase.logd("getEsimGPIOState mESimState == " + mESimState);
        return mESimState;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public String getProductEid() {
        mEID = "";
        if (ModemProvider.IS_QCOM) {
            return mEID;
        }
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("getProductEid");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_GET_ESIM_EID"}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.4
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                String[] strArr = (String[]) asyncResult.result;
                                if (strArr != null && strArr.length == 1) {
                                    String[] strArrSplit = strArr[0].split(",");
                                    MiuiEsimManagerBase.logd("getProductEid ActualResult = " + TelephonyUtils.pii(Arrays.toString(strArrSplit)) + " , data length = " + strArrSplit.length);
                                    if ("-1".equalsIgnoreCase(strArrSplit[0]) || "-2".equalsIgnoreCase(strArrSplit[0]) || "-3".equalsIgnoreCase(strArrSplit[0])) {
                                        MiuiEsimManager.mEID = "";
                                    } else {
                                        MiuiEsimManager.mEID = strArrSplit[0];
                                    }
                                }
                                MiuiEsimManagerBase.logi("getProductEid ActualResult = " + TelephonyUtils.pii(MiuiEsimManager.mEID));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MiuiEsimManagerBase.logi("getProductEid final eid = " + TelephonyUtils.pii(mEID));
        return mEID;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public void writeProductEid(byte[] bArr) {
        if (ModemProvider.IS_QCOM) {
            MiuiEsimManagerBase.logi("writeProductEid error");
            return;
        }
        String hexString = HexDump.toHexString(bArr);
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("writeProductEid");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_SET_ESIM_EID", hexString}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.5
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                String[] strArr = (String[]) asyncResult.result;
                                MiuiEsimManagerBase.trackerEsimEvent("writeProductEid", strArr);
                                MiuiEsimManagerBase.logi("writeProductEid ActualResult = " + Arrays.toString(strArr));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public String[] openChannelEsim() {
        if (ModemProvider.IS_QCOM) {
            return super.openChannelEsim();
        }
        mOpenChannelResult = new String[]{""};
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("openChannelEsim");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_ESIM_REQUEST", "1"}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.6
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                MiuiEsimManager.mOpenChannelResult = (String[]) asyncResult.result;
                                MiuiEsimManagerBase.logd("openChannelEsim mOpenChannelResult = " + Arrays.toString(MiuiEsimManager.mOpenChannelResult) + " , mOpenChannelResult length = " + MiuiEsimManager.mOpenChannelResult.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mOpenChannelResult;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public String[] closeChannelEsim(int i) {
        if (ModemProvider.IS_QCOM) {
            return super.closeChannelEsim(i);
        }
        mCloseChannelResult = new String[]{""};
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("closeChannelEsim");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_ESIM_REQUEST", "3", String.valueOf(i)}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.7
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                MiuiEsimManager.mCloseChannelResult = (String[]) asyncResult.result;
                                MiuiEsimManagerBase.logd("closeChannelEsim mCloseChannelResult = " + Arrays.toString(MiuiEsimManager.mCloseChannelResult) + " , mCloseChannelResult length = " + MiuiEsimManager.mCloseChannelResult.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mCloseChannelResult;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public String[] sendApduEsim(int i, String str) {
        if (ModemProvider.IS_QCOM) {
            return super.sendApduEsim(i, str);
        }
        mSendAPDUResult = new String[]{""};
        Phone phone = MiuiPhoneUtils.getPhone(1);
        if (phone != null) {
            HandlerThread handlerThread = new HandlerThread("sendApduEsim");
            handlerThread.start();
            try {
                ATCommandUtils.getInstance().sendAtCommand(phone, new String[]{"MIPC_ESIM_REQUEST", "2", String.valueOf(i), str}, new Handler(this, handlerThread.getLooper()) { // from class: com.android.phone.MiuiEsimManager.8
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (asyncResult != null && asyncResult.exception == null) {
                            try {
                                MiuiEsimManager.mSendAPDUResult = (String[]) asyncResult.result;
                                MiuiEsimManagerBase.logd("sendApduEsim ActualResult = " + TelephonyUtils.pii(Arrays.toString(MiuiEsimManager.mSendAPDUResult)) + " , mSendAPDUResult length = " + MiuiEsimManager.mSendAPDUResult.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getLooper().quit();
                    }
                }.obtainMessage());
                handlerThread.join(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mSendAPDUResult;
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public void resetPortAndStateForSlot(int i) {
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlot(i);
        if (uiccSlot != null) {
            uiccSlot.resetPortAndState();
        }
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public boolean shouldResetPortAndState() {
        return MiuiPhoneUtils.isSupportMep() && isDualEsimActive();
    }

    @Override // com.android.phone.MiuiEsimManagerBase
    public int modifyEuiccSVN(int i) {
        synchronized (MiuiEsimManagerBase.mLock) {
            try {
                int iOpenChannel = openChannel();
                if (iOpenChannel >= 256) {
                    return 1;
                }
                String euiccInfoVersion = getEuiccInfoVersion(mChannelId);
                if (2 == i && "020500".equals(euiccInfoVersion)) {
                    iOpenChannel = sendAndParseApdu(i, mChannelId);
                    MiuiEsimManagerBase.logd("The result of sendAndParseApdu actionType :" + i + " , result:" + iOpenChannel);
                    if (iOpenChannel == 0) {
                        iOpenChannel = sendAndParseApdu(3, mChannelId);
                        if (iOpenChannel == 0 && "020300".equals(mCurrentSVN)) {
                            MiuiEsimManagerBase.setSVNDowngradeStatus(1);
                        } else {
                            iOpenChannel = 1;
                        }
                    }
                }
                closeChannel(mChannelId);
                return iOpenChannel;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private String getEuiccInfoVersion(int i) {
        if (sendAndParseApdu(3, i) == 0) {
            return mCurrentSVN;
        }
        return "020500";
    }

    public static int closeChannel(int i) {
        try {
            int i2 = Integer.parseInt(MiuiEsimManagerBase.getInstance().closeChannelEsim(i)[1].trim(), 16);
            MiuiEsimManagerBase.logi("Result of Close Channel:" + Integer.toHexString(i2));
            return i2 == 36864 ? 0 : 4;
        } catch (Exception e) {
            MiuiEsimManagerBase.loge("Close channel exception:" + e);
            return 4;
        }
    }

    private static int openChannel() {
        try {
            String[] strArrOpenChannelEsim = MiuiEsimManagerBase.getInstance().openChannelEsim();
            for (String str : strArrOpenChannelEsim) {
                MiuiEsimManagerBase.logi("The parameter returned from modem: " + str);
            }
            int i = Integer.parseInt(strArrOpenChannelEsim[1].trim(), 16);
            MiuiEsimManagerBase.logi("statusWord:" + Integer.toHexString(i));
            if (i != 36864) {
                if (i != 27265) {
                    return DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER;
                }
                MiuiEsimManagerBase.logi("Open channel failed without any free channel");
                return 257;
            }
            int iIntValue = Integer.valueOf(strArrOpenChannelEsim[0].trim()).intValue();
            MiuiEsimManagerBase.logi("Open channel successfully, channel ID:" + iIntValue);
            mChannelId = iIntValue;
            return iIntValue;
        } catch (Exception e) {
            MiuiEsimManagerBase.loge("Open channel failed, error code:" + e);
            return DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER;
        }
    }

    private int sendAndParseApdu(int i, int i2) {
        if (i == 1) {
            return sendApduForType(i, i2, Utils.bytesToHexString(UPGRADE_VERSION_COMMAND));
        }
        if (i == 2) {
            return sendApduForType(i, i2, Utils.bytesToHexString(DOWNGRADE_VERSION_COMMAND));
        }
        if (i != 3) {
            return -1;
        }
        return sendApduForType(i, i2, Utils.bytesToHexString(GET_INFO1_COMMAND));
    }

    private List parseAllProfilesResponse(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        try {
            Asn1Node asn1NodeNextNode = new Asn1Decoder(bArr).nextNode();
            if (!asn1NodeNextNode.hasChild(160, new int[0])) {
                MiuiEsimManagerBase.logi("parseAllProfilesResponse: no profile data in response");
                return arrayList;
            }
            for (Asn1Node asn1Node : asn1NodeNextNode.getChild(160, new int[0]).getChildren(227)) {
                try {
                    byte[] bArrAsBytes = asn1Node.getChild(90, new int[0]).asBytes();
                    String strStripTrailingFs = IccUtils.stripTrailingFs(IccUtils.bchToString(bArrAsBytes, 0, bArrAsBytes.length));
                    EuiccProfileInfo.Builder builder = new EuiccProfileInfo.Builder(strStripTrailingFs);
                    if (asn1Node.hasChild(40740, new int[0]) && asn1Node.getChild(40740, new int[0]).asInteger() >= 0) {
                        builder.setState(1);
                    } else if (asn1Node.hasChild(40816, new int[0])) {
                        builder.setState(asn1Node.getChild(40816, new int[0]).asInteger());
                    } else {
                        builder.setState(0);
                    }
                    EuiccProfileInfo euiccProfileInfoBuild = builder.build();
                    arrayList.add(euiccProfileInfoBuild);
                    MiuiEsimManagerBase.logi("parseAllProfilesResponse: iccid=" + TelephonyUtils.pii(strStripTrailingFs) + ", state=" + euiccProfileInfoBuild.getState());
                } catch (Exception e) {
                    MiuiEsimManagerBase.loge("parseAllProfilesResponse: error parsing profile node: " + e);
                }
            }
        } catch (Exception e2) {
            MiuiEsimManagerBase.loge("parseAllProfilesResponse: error parsing response: " + e2);
        }
        return arrayList;
    }

    private String buildDisableProfileCommand(String str) {
        if (str == null || str.isEmpty()) {
            MiuiEsimManagerBase.loge("buildDisableProfileCommand: iccid is null or empty");
            return null;
        }
        try {
            if (str.length() < 20) {
                str = str + new String(new char[20 - str.length()]).replace((char) 0, 'F');
            }
            return Asn1Node.newBuilder(48946).addChild(Asn1Node.newBuilder(160).addChildAsBytes(90, IccUtils.bcdToBytes(str))).addChildAsBoolean(129, false).build().toHex();
        } catch (Exception e) {
            MiuiEsimManagerBase.loge("buildDisableProfileCommand: error: " + e);
            return null;
        }
    }

    private int disableEsimProfile(String str) {
        byte[] bArrHexStringToBytes;
        String strBuildDisableProfileCommand = buildDisableProfileCommand(str);
        if (strBuildDisableProfileCommand == null) {
            MiuiEsimManagerBase.loge("disableEsimProfile: build command failed for iccid=" + TelephonyUtils.pii(str));
            return 1;
        }
        int iOpenChannel = -1;
        try {
            try {
                iOpenChannel = openChannel();
                if (iOpenChannel >= 256) {
                    MiuiEsimManagerBase.loge("disableEsimProfile: open channel failed, result=" + iOpenChannel);
                    if (iOpenChannel >= 0 && iOpenChannel < 256) {
                        closeChannel(iOpenChannel);
                        MiuiEsimManagerBase.logi("disableEsimProfile: channel closed");
                    }
                    return 1;
                }
                MiuiEsimManagerBase.logi("disableEsimProfile: channel opened, channelId=" + iOpenChannel);
                String[] strArrSendApduEsim = sendApduEsim(iOpenChannel, strBuildDisableProfileCommand);
                if (strArrSendApduEsim != null && strArrSendApduEsim.length >= 2 && "9000".equals(strArrSendApduEsim[1].trim()) && (bArrHexStringToBytes = Utils.hexStringToBytes(strArrSendApduEsim[0].trim())) != null && bArrHexStringToBytes.length > 0) {
                    int iAsInteger = new Asn1Decoder(bArrHexStringToBytes).nextNode().getChild(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, new int[0]).asInteger();
                    MiuiEsimManagerBase.logi("disableEsimProfile: resultCode=" + iAsInteger + " for iccid=" + TelephonyUtils.pii(str));
                    if (iAsInteger == 0) {
                        if (iOpenChannel >= 0 && iOpenChannel < 256) {
                            closeChannel(iOpenChannel);
                            MiuiEsimManagerBase.logi("disableEsimProfile: channel closed");
                        }
                        return 0;
                    }
                }
                MiuiEsimManagerBase.loge("disableEsimProfile: APDU failed for iccid=" + TelephonyUtils.pii(str));
                if (iOpenChannel >= 0 && iOpenChannel < 256) {
                    closeChannel(iOpenChannel);
                    MiuiEsimManagerBase.logi("disableEsimProfile: channel closed");
                }
                return 1;
            } catch (Exception e) {
                MiuiEsimManagerBase.loge("disableEsimProfile: exception: " + e);
                if (-1 >= 0 && -1 < 256) {
                }
            }
        } catch (Throwable th) {
            if (-1 >= 0 && -1 < 256) {
                closeChannel(-1);
                MiuiEsimManagerBase.logi("disableEsimProfile: channel closed");
            }
            throw th;
        }
    }

    public int disableEnabledEsimProfiles() {
        synchronized (MiuiEsimManagerBase.mLock) {
            try {
                List allProfilesByApdu = getAllProfilesByApdu();
                if (allProfilesByApdu.isEmpty()) {
                    MiuiEsimManagerBase.logi("disableEnabledEsimProfiles: no profiles found");
                    return 0;
                }
                return disableActivatedProfiles(allProfilesByApdu);
            } catch (Exception e) {
                MiuiEsimManagerBase.loge("disableEnabledEsimProfiles: exception: " + e);
                return 1;
            }
        }
    }

    private List getAllProfilesByApdu() {
        List arrayList;
        try {
            try {
                int iOpenChannel = openChannel();
                if (iOpenChannel >= 256) {
                    MiuiEsimManagerBase.loge("getAllProfilesByApdu: open channel failed, result=" + iOpenChannel);
                    arrayList = new ArrayList();
                    if (iOpenChannel >= 0 && iOpenChannel < 256) {
                    }
                    return arrayList;
                }
                MiuiEsimManagerBase.logi("getAllProfilesByApdu: channel opened, channelId=" + iOpenChannel);
                String[] strArrSendApduEsim = sendApduEsim(iOpenChannel, "BF2D0F5C0D5A909192B79F709599BF769F24");
                if (strArrSendApduEsim == null || strArrSendApduEsim.length < 2 || !"9000".equals(strArrSendApduEsim[1].trim())) {
                    MiuiEsimManagerBase.loge("getAllProfilesByApdu: APDU failed");
                    arrayList = new ArrayList();
                    if (iOpenChannel < 0 || iOpenChannel >= 256) {
                        return arrayList;
                    }
                } else {
                    byte[] bArrHexStringToBytes = Utils.hexStringToBytes(strArrSendApduEsim[0].trim());
                    if (bArrHexStringToBytes == null || bArrHexStringToBytes.length == 0) {
                        MiuiEsimManagerBase.logi("getAllProfilesByApdu: empty response");
                        arrayList = new ArrayList();
                        if (iOpenChannel < 0 || iOpenChannel >= 256) {
                            return arrayList;
                        }
                    } else {
                        arrayList = parseAllProfilesResponse(bArrHexStringToBytes);
                        if (iOpenChannel < 0 || iOpenChannel >= 256) {
                            return arrayList;
                        }
                    }
                }
                closeChannel(iOpenChannel);
                MiuiEsimManagerBase.logi("getAllProfilesByApdu: channel closed");
                return arrayList;
            } catch (Exception e) {
                MiuiEsimManagerBase.loge("getAllProfilesByApdu: exception: " + e);
                ArrayList arrayList2 = new ArrayList();
                if (-1 >= 0 && -1 < 256) {
                    closeChannel(-1);
                    MiuiEsimManagerBase.logi("getAllProfilesByApdu: channel closed");
                }
                return arrayList2;
            }
        } catch (Throwable th) {
            if (-1 >= 0 && -1 < 256) {
                closeChannel(-1);
                MiuiEsimManagerBase.logi("getAllProfilesByApdu: channel closed");
            }
            throw th;
        }
    }

    private int disableActivatedProfiles(List list) {
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            EuiccProfileInfo euiccProfileInfo = (EuiccProfileInfo) it.next();
            if (euiccProfileInfo.getState() == 1) {
                MiuiEsimManagerBase.logi("disableActivatedProfiles: disabling iccid=" + TelephonyUtils.pii(euiccProfileInfo.getIccid()));
                if (disableEsimProfile(euiccProfileInfo.getIccid()) != 0) {
                    MiuiEsimManagerBase.loge("disableActivatedProfiles: disable failed for iccid=" + TelephonyUtils.pii(euiccProfileInfo.getIccid()));
                    i = 1;
                }
            }
        }
        return i;
    }

    private int sendApduForType(int i, int i2, String str) {
        byte[] bArrHexStringToBytes;
        try {
            if (i == 1 || i == 2) {
                String[] strArrSendApduEsim = MiuiEsimManagerBase.getInstance().sendApduEsim(i2, str);
                if (strArrSendApduEsim[1].trim().equals("9000") && (bArrHexStringToBytes = Utils.hexStringToBytes(strArrSendApduEsim[0].trim())) != null && bArrHexStringToBytes.length == 4) {
                    byte b = bArrHexStringToBytes[3];
                    MiuiEsimManagerBase.logd("The response code of modify Euicc SVN:" + ((int) b));
                    if (b == 0) {
                        return 0;
                    }
                }
                return 1;
            }
            if (i != 3) {
                return -1;
            }
            String[] strArrSendApduEsim2 = MiuiEsimManagerBase.getInstance().sendApduEsim(i2, str);
            if (strArrSendApduEsim2[1].trim().equals("9000")) {
                byte[] value = new MiuiEsimManagerBase.BerTlv(Utils.hexStringToBytes(strArrSendApduEsim2[0].trim())).find(130).getValue();
                MiuiEsimManagerBase.logd("The APDU_CMD_GET_INFO1 response code of curEuiccSvn :" + Arrays.toString(value));
                if (value != null) {
                    String strBytesToHexString = Utils.bytesToHexString(value);
                    mCurrentSVN = strBytesToHexString;
                    if ("020300".equals(strBytesToHexString) || "020500".equals(mCurrentSVN)) {
                        return 0;
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            MiuiEsimManagerBase.loge("Exception occurs, error code:" + e);
            return 3;
        }
    }

    private static boolean isLui(PackageManager packageManager) {
        Bundle bundle;
        try {
            ApplicationInfo applicationInfo = packageManager.getPackageInfo("com.miui.euicc", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).applicationInfo;
            if (applicationInfo == null || (bundle = applicationInfo.metaData) == null) {
                return false;
            }
            String string = bundle.getString("mi_lpa_tech_version");
            MiuiEsimManagerBase.logi("techVersion = " + string);
            return "lui".equals(string);
        } catch (Exception e) {
            MiuiEsimManagerBase.loge("isLui: failed to check LUI version: " + e);
            return false;
        }
    }
}
