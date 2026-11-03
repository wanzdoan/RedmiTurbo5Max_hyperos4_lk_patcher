package com.android.phone;

import android.os.Handler;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.internal.hyperradio.RadioResponseData;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.LinkedHashMap;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ModemTcpKeepAliveController extends ModemTcpKaBase {
    private final HyperRadioManager mHyperRadio = HyperRadioManager.getInstance(this.mContext);

    protected ModemTcpKeepAliveController() {
        ModemTcpKaBase.logd("ModemTcpKeepAliveController init");
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        updateDefaultDataSlotId();
        int i4 = this.mDdsPhoneId;
        HyperRadioManager hyperRadioManager = this.mHyperRadio;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        hyperRadioManager.startTcpMonitorRegister(str, i, str2, i2, i3, i4, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new AnonymousClass1(i3, i4));
        return 0;
    }

    /* JADX INFO: renamed from: com.android.phone.ModemTcpKeepAliveController$1, reason: invalid class name */
    class AnonymousClass1 extends HyperRadioManager.GenericResponse {
        final /* synthetic */ int val$clientId;
        final /* synthetic */ int val$phoneId;

        AnonymousClass1(int i, int i2) {
            this.val$clientId = i;
            this.val$phoneId = i2;
        }

        public void onResponse(int i, RadioResponseData radioResponseData) {
            if (radioResponseData == null) {
                ModemTcpKaBase.logd("RadioResponseData is null! startTcpMonitorRegister failed");
                return;
            }
            int iIntValue = radioResponseData.getIntValue().intValue();
            ModemTcpKaBase.logd("startTcpMonitorRegister, result=" + iIntValue + " clientId=" + this.val$clientId);
            ModemTcpKeepAliveController.this.localLog("startTcpMonitorRegister, result=" + iIntValue + " clientId=" + this.val$clientId);
            if (iIntValue == 0) {
                Handler handler = ModemTcpKeepAliveController.this.mHandler;
                final int i2 = this.val$phoneId;
                final int i3 = this.val$clientId;
                handler.post(new Runnable() { // from class: com.android.phone.ModemTcpKeepAliveController$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onResponse$0(i2, i3);
                    }
                });
                ModemTcpKaBase.logd("startTcpMonitorRegister, mDdsPhoneId = " + this.val$phoneId + ", clientId = " + this.val$clientId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$0(int i, int i2) {
            ModemTcpKeepAliveController.this.mDataToClientMap.put(Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpMonitorRegister(int i) {
        int i2 = this.mDdsPhoneId;
        HyperRadioManager hyperRadioManager = this.mHyperRadio;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        hyperRadioManager.stopTcpMonitorRegister(i, i2, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new AnonymousClass2(i, i2));
        return 0;
    }

    /* JADX INFO: renamed from: com.android.phone.ModemTcpKeepAliveController$2, reason: invalid class name */
    class AnonymousClass2 extends HyperRadioManager.GenericResponse {
        final /* synthetic */ int val$clientId;
        final /* synthetic */ int val$phoneId;

        AnonymousClass2(int i, int i2) {
            this.val$clientId = i;
            this.val$phoneId = i2;
        }

        public void onResponse(int i, RadioResponseData radioResponseData) {
            if (radioResponseData == null) {
                ModemTcpKaBase.logd("RadioResponseData is null! stopTcpMonitorRegister failed");
                return;
            }
            int iIntValue = radioResponseData.getIntValue().intValue();
            ModemTcpKaBase.logd("stopTcpMonitorRegister, result=" + iIntValue + " clientId=" + this.val$clientId + " phoneId=" + this.val$phoneId);
            ModemTcpKeepAliveController modemTcpKeepAliveController = ModemTcpKeepAliveController.this;
            StringBuilder sb = new StringBuilder("stopTcpMonitorRegister, result=");
            sb.append(iIntValue);
            sb.append(" clientId=");
            sb.append(this.val$clientId);
            modemTcpKeepAliveController.localLog(sb.toString());
            if (iIntValue == 0) {
                ModemTcpKeepAliveController.this.mHandler.post(new Runnable() { // from class: com.android.phone.ModemTcpKeepAliveController$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onResponse$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$0() {
            ModemTcpKeepAliveController.this.mStartMonitorPhoneIdList.clear();
            ModemTcpKeepAliveController.this.mDataToClientMap.clear();
        }
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        this.mDlyKaTrigCnt++;
        this.mFlgWatingKaInd = true;
        this.mHandler.sendEmptyMessageDelayed(3, 3000L);
        int i5 = this.mDdsPhoneId;
        HyperRadioManager hyperRadioManager = this.mHyperRadio;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        hyperRadioManager.startTcpKeepAlive(str, i, str2, i2, i3, i4, i5, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new AnonymousClass3(i4, i5));
        return 0;
    }

    /* JADX INFO: renamed from: com.android.phone.ModemTcpKeepAliveController$3, reason: invalid class name */
    class AnonymousClass3 extends HyperRadioManager.GenericResponse {
        final /* synthetic */ int val$clientId;
        final /* synthetic */ int val$phoneId;

        AnonymousClass3(int i, int i2) {
            this.val$clientId = i;
            this.val$phoneId = i2;
        }

        public void onResponse(int i, RadioResponseData radioResponseData) {
            if (radioResponseData == null) {
                ModemTcpKaBase.logd("RadioResponseData is null! startTcpKeepAlive failed");
                return;
            }
            final int iIntValue = radioResponseData.getIntValue().intValue();
            ModemTcpKaBase.logd("startTcpKeepAlive, result=" + iIntValue + " clientId=" + this.val$clientId + "phoneId=" + this.val$phoneId);
            ModemTcpKeepAliveController modemTcpKeepAliveController = ModemTcpKeepAliveController.this;
            StringBuilder sb = new StringBuilder("startTcpKeepAlive, result=");
            sb.append(iIntValue);
            sb.append(" clientId=");
            sb.append(this.val$clientId);
            modemTcpKeepAliveController.localLog(sb.toString());
            Handler handler = ModemTcpKeepAliveController.this.mHandler;
            final int i2 = this.val$phoneId;
            handler.post(new Runnable() { // from class: com.android.phone.ModemTcpKeepAliveController$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onResponse$0(iIntValue, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$0(int i, int i2) {
            if (i == 0) {
                ModemTcpKeepAliveController.this.mStartMonitorPhoneIdList.add(Integer.valueOf(i2));
            }
            ModemTcpKeepAliveController.this.mHandler.removeMessages(3);
            String resultFromValue = ModemTcpKeepAliveController.this.getResultFromValue(i);
            ModemTcpKeepAliveController.this.dataTrackingForKaInd(resultFromValue);
            ModemTcpKeepAliveController.this.notifyListeners(resultFromValue);
        }
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpKeepAlive(final int i) {
        this.mKaStopTime = System.currentTimeMillis();
        dataTrackingForDuration();
        final int i2 = this.mDdsPhoneId;
        HyperRadioManager hyperRadioManager = this.mHyperRadio;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        hyperRadioManager.stopTcpKeepAlive(i, i2, new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.ModemTcpKeepAliveController.4
            public void onResponse(int i3, RadioResponseData radioResponseData) {
                if (radioResponseData == null) {
                    ModemTcpKaBase.logd("RadioResponseData is null! stopTcpKeepAlive failed");
                    return;
                }
                int iIntValue = radioResponseData.getIntValue().intValue();
                ModemTcpKaBase.logd("stopTcpKeepAlive, ret=" + iIntValue + " clientId=" + i + " phoneId=" + i2);
                ModemTcpKeepAliveController modemTcpKeepAliveController = ModemTcpKeepAliveController.this;
                StringBuilder sb = new StringBuilder("stopTcpKeepAlive, ret=");
                sb.append(iIntValue);
                sb.append(" clientId=");
                sb.append(i);
                modemTcpKeepAliveController.localLog(sb.toString());
            }
        });
        return 0;
    }

    @Override // com.android.phone.ModemTcpKaBase
    protected void recordStatisticsEvent() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_cellular_tcp_keepavlie", new LinkedHashMap(this.mDataTackingMap));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.ModemTcpKaBase
    /* JADX INFO: renamed from: tcpMonitorRelease */
    public void lambda$onCloudChanged$0() {
        ModemTcpKaBase.logd("tcpMonitorRelease begin");
        for (Integer num : this.mStartMonitorPhoneIdList) {
            if (this.mDataToClientMap.containsKey(num)) {
                Integer num2 = (Integer) this.mDataToClientMap.get(num);
                stopTcpKeepAlive(num2.intValue());
                stopTcpMonitorRegister(num2.intValue());
            }
        }
        this.mStartMonitorPhoneIdList.clear();
        this.mDataToClientMap.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getResultFromValue(int i) {
        if (i != 0) {
            if (i != 1) {
                ModemTcpKaBase.logd("invalid result.");
            }
            return "KEEP_ALIVE_RESULT_ERROR";
        }
        return "KEEP_ALIVE_RESULT_OK";
    }
}
