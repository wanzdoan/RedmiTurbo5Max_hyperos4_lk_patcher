package com.android.phone;

import android.os.AsyncResult;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/* JADX INFO: loaded from: classes.dex */
public class QcModemTcpKeepAliveController extends ModemTcpKaBase {
    protected ModemUtils mModemUtils = ModemUtils.init(this.mContext);

    protected QcModemTcpKeepAliveController() {
        ModemTcpKaBase.logd("QcModemTcpKeepAliveController init");
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        if (this.mModemUtils == null) {
            ModemTcpKaBase.loge("startTcpMonitorRegister, mModemUtils is null, clientId=" + i3);
            return 1;
        }
        if (!registerTcpKaIndHandler()) {
            ModemTcpKaBase.loge("startTcpMonitorRegister, registerTcpKaIndHandler failed, clientId=" + i3);
            return 1;
        }
        updateDefaultDataSlotId();
        Object objCallHook = this.mModemUtils.callHook("startTcpMonitorRegister", str, Integer.valueOf(i), str2, Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof Integer) {
            int iIntValue = ((Integer) objCallHook).intValue();
            ModemTcpKaBase.logd("startTcpMonitorRegister, ret=" + iIntValue + " clientId=" + i3);
            localLog("startTcpMonitorRegister, ret=" + iIntValue + " clientId=" + i3);
            if (iIntValue == 0) {
                this.mDataToClientMap.put(Integer.valueOf(this.mDdsPhoneId), Integer.valueOf(i3));
                ModemTcpKaBase.logd("startTcpMonitorRegister, mDdsPhoneId = " + this.mDdsPhoneId + ", clientId = " + i3);
            }
            return iIntValue;
        }
        StringBuilder sb = new StringBuilder("startTcpMonitorRegister: return type not Integer, actual type: ");
        sb.append(objCallHook != null ? objCallHook.getClass().getName() : "null");
        sb.append(", clientId=");
        sb.append(i3);
        ModemTcpKaBase.loge(sb.toString());
        return 1;
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpMonitorRegister(int i) {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            ModemTcpKaBase.loge("stopTcpMonitorRegister, mModemUtils is null, clientId=" + i);
            return 1;
        }
        Object objCallHook = modemUtils.callHook("stopTcpMonitorRegister", Integer.valueOf(i), Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof Integer) {
            int iIntValue = ((Integer) objCallHook).intValue();
            ModemTcpKaBase.logd("stopTcpMonitorRegister, ret=" + iIntValue + " clientId=" + i);
            localLog("stopTcpMonitorRegister, ret=" + iIntValue + " clientId=" + i);
            if (iIntValue == 0) {
                this.mStartMonitorPhoneIdList.clear();
                this.mDataToClientMap.clear();
            }
            return iIntValue;
        }
        StringBuilder sb = new StringBuilder("stopTcpMonitorRegister: return type not Integer, actual type: ");
        sb.append(objCallHook != null ? objCallHook.getClass().getName() : "null");
        sb.append(", clientId=");
        sb.append(i);
        ModemTcpKaBase.loge(sb.toString());
        return 1;
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        if (this.mModemUtils == null) {
            ModemTcpKaBase.loge("startTcpKeepAlive, mModemUtils is null, clientId=" + i4);
            return 1;
        }
        this.mDlyKaTrigCnt++;
        this.mFlgWatingKaInd = true;
        this.mHandler.sendEmptyMessageDelayed(3, 3000L);
        Object objCallHook = this.mModemUtils.callHook("startTcpKeepAlive", str, Integer.valueOf(i), str2, Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof Integer) {
            int iIntValue = ((Integer) objCallHook).intValue();
            ModemTcpKaBase.logd("startTcpKeepAlive, ret=" + iIntValue + " clientId=" + i4);
            localLog("startTcpKeepAlive, ret=" + iIntValue + " clientId=" + i4);
            return iIntValue;
        }
        StringBuilder sb = new StringBuilder("startTcpKeepAlive: return type not Integer, actual type: ");
        sb.append(objCallHook != null ? objCallHook.getClass().getName() : "null");
        sb.append(", clientId=");
        sb.append(i4);
        ModemTcpKaBase.loge(sb.toString());
        return 1;
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpKeepAlive(int i) {
        if (this.mModemUtils == null) {
            ModemTcpKaBase.loge("stopTcpKeepAlive, mModemUtils is null, clientId=" + i);
            return 1;
        }
        this.mKaStopTime = System.currentTimeMillis();
        dataTrackingForDuration();
        Object objCallHook = this.mModemUtils.callHook("stopTcpKeepAlive", Integer.valueOf(i), Integer.valueOf(this.mDdsPhoneId));
        if (objCallHook instanceof Integer) {
            int iIntValue = ((Integer) objCallHook).intValue();
            ModemTcpKaBase.logd("stopTcpKeepAlive, ret=" + iIntValue + " clientId=" + i);
            localLog("stopTcpKeepAlive, ret=" + iIntValue + " clientId=" + i);
            return iIntValue;
        }
        StringBuilder sb = new StringBuilder("stopTcpKeepAlive: return type not Integer, actual type: ");
        sb.append(objCallHook != null ? objCallHook.getClass().getName() : "null");
        sb.append(", clientId=");
        sb.append(i);
        ModemTcpKaBase.loge(sb.toString());
        return 1;
    }

    private boolean registerTcpKaIndHandler() {
        if (!this.mTcpKaRegisterFlag && this.mModemUtils != null) {
            for (int i = 0; i < this.mPhoneCount; i++) {
                this.mModemUtils.registerOemHookRegistrant(this.mHandler, 1, Integer.valueOf(i), "qualcomm.intent.action.ACTION_TCP_KA_IND", i);
            }
            this.mTcpKaRegisterFlag = true;
        }
        return this.mTcpKaRegisterFlag;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.ModemTcpKaBase
    /* JADX INFO: renamed from: tcpMonitorRelease */
    public void lambda$onCloudChanged$0() {
        int iIntValue;
        ModemTcpKaBase.logd("tcpMonitorRelease begin");
        if (this.mModemUtils == null) {
            ModemTcpKaBase.logd("tcpMonitorRelease failed, mModemUtils is null");
            return;
        }
        for (Integer num : this.mStartMonitorPhoneIdList) {
            if (this.mDataToClientMap.containsKey(num)) {
                Integer num2 = (Integer) this.mDataToClientMap.get(num);
                Object objCallHook = this.mModemUtils.callHook("stopTcpKeepAlive", num2, num);
                int iIntValue2 = 1;
                if (objCallHook instanceof Integer) {
                    iIntValue = ((Integer) objCallHook).intValue();
                } else {
                    StringBuilder sb = new StringBuilder("tcpMonitorRelease: stopTcpKeepAlive return not Integer, actual: ");
                    sb.append(objCallHook != null ? objCallHook.getClass().getName() : "null");
                    sb.append(", phoneId=");
                    sb.append(num);
                    sb.append(", clientId=");
                    sb.append(num2);
                    ModemTcpKaBase.loge(sb.toString());
                    iIntValue = 1;
                }
                Object objCallHook2 = this.mModemUtils.callHook("stopTcpMonitorRegister", num2, Integer.valueOf(this.mDdsPhoneId));
                if (objCallHook2 instanceof Integer) {
                    iIntValue2 = ((Integer) objCallHook2).intValue();
                } else {
                    StringBuilder sb2 = new StringBuilder("tcpMonitorRelease: stopTcpMonitorRegister return not Integer, actual: ");
                    sb2.append(objCallHook2 != null ? objCallHook2.getClass().getName() : "null");
                    sb2.append(", phoneId=");
                    sb2.append(num);
                    sb2.append(", clientId=");
                    sb2.append(num2);
                    ModemTcpKaBase.loge(sb2.toString());
                }
                ModemTcpKaBase.logd(String.format("tcpMonitorRelease, phoneId: %d, clientId: %d, stopKARet: %d, stopMonitorRet: %d", num, num2, Integer.valueOf(iIntValue), Integer.valueOf(iIntValue2)));
            }
        }
        this.mStartMonitorPhoneIdList.clear();
        this.mDataToClientMap.clear();
    }

    @Override // com.android.phone.ModemTcpKaBase
    protected void recordStatisticsEvent() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_cellular_tcp_keepavlie", new LinkedHashMap(this.mDataTackingMap));
    }

    @Override // com.android.phone.ModemTcpKaBase
    protected void onTcpKeepAliveInd(AsyncResult asyncResult) {
        if (asyncResult.exception == null) {
            try {
                Object obj = asyncResult.result;
                if (obj instanceof byte[]) {
                    String str = new String(ByteBuffer.wrap((byte[]) obj).array());
                    ModemTcpKaBase.logd("tcp ka ind result = ".concat(str));
                    if (str.equals("KEEP_ALIVE_RESULT_OK")) {
                        Integer num = (Integer) asyncResult.userObj;
                        ModemTcpKaBase.logd("tcp ka ind phoneId = " + num);
                        this.mStartMonitorPhoneIdList.add(num);
                    }
                    this.mHandler.removeMessages(3);
                    dataTrackingForKaInd(str);
                    notifyListeners(str);
                    return;
                }
                ModemTcpKaBase.loge("Unexpected ka ind type");
                return;
            } catch (Exception e) {
                ModemTcpKaBase.loge("Error occurred while parsing result");
                e.printStackTrace();
                return;
            }
        }
        ModemTcpKaBase.loge("tcp ka ind failed");
    }
}
