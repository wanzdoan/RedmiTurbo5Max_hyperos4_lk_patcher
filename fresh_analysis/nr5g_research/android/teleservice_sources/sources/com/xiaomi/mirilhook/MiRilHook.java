package com.xiaomi.mirilhook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.PhoneFactory;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class MiRilHook extends MiRilHookBase {
    public static final int DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID = 20066;
    public static final int DEFAULT_CUST_XIAOMI_LOG_AUTO_GRAB_APP_ID = 20067;
    public static final int EVENT_MTK_OEMHOOK_AT_CMD_SEND = 3;
    public static final int EVENT_MTK_OEMHOOK_PROP_GET = 1;
    public static final int EVENT_MTK_OEMHOOK_PROP_SET = 2;
    public static final int EVENT_OEMHOOK_NULL = 0;
    public static final int HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT = 104;
    public static final int HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT = 103;
    public static final int HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT = 105;
    public static final int HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT = 102;
    public static final int HOOK_ID_START_DMF_BY_AT = 100;
    public static final int HOOK_ID_START_URC_BY_AT = 300;
    public static final int HOOK_ID_STOP_DMF_BY_AT = 101;
    public static final int HOOK_ID_STOP_URC_BY_AT = 301;
    private static final String LOG_TAG = "MiRilHook-MTK";
    private static final int MODEM_STATS_DATA_INVALID = 65535;
    private static final int MTK_RESPONSE_BYTE_SIZE = 102400;
    protected static final String XIAOMI_MTK_HOOK = "XIAOMI_MTK_HOOK";
    private El1PuschPowerControl mEl1PuschPowerControlSub0;
    private El1PuschPowerControl mEl1PuschPowerControlSub1;
    private El1ServingCellMeasurement mEl1ServCellInfoSub0;
    private El1ServingCellMeasurement mEl1ServCellInfoSub1;
    protected Handler mHandler;
    private Handler mMtkHandler;
    private MtkTelephonyManagerEx mMtkTmEx;
    private Nl1PuschPowerControl mNl1PuschPowerControlSub0;
    private Nl1PuschPowerControl mNl1PuschPowerControlSub1;
    private Nl1ServingCellMeasurement mNl1ServCellInfoSub0;
    private Nl1ServingCellMeasurement mNl1ServCellInfoSub1;

    public static int getHookType() {
        return 1;
    }

    public MiRilHook(Context context, Handler handler) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                MiRilHook.log("handleMessage msg id: " + message.what);
                new Bundle();
                message.getData();
                AsyncResult asyncResult = (AsyncResult) message.obj;
                switch (message.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(asyncResult);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(asyncResult);
                        break;
                }
            }
        };
        this.mNl1ServCellInfoSub0 = new Nl1ServingCellMeasurement(this);
        this.mNl1ServCellInfoSub1 = new Nl1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub0 = new El1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub1 = new El1ServingCellMeasurement(this);
        this.mEl1PuschPowerControlSub0 = new El1PuschPowerControl(this);
        this.mEl1PuschPowerControlSub1 = new El1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub0 = new Nl1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub1 = new Nl1PuschPowerControl(this);
        MiRilHookBase.mContext = context;
        log("MiRilHook, context = " + context + ", handler = " + handler);
        if (MiRilHookBase.mContext == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context is null");
            throw null;
        }
        this.mMtkHandler = handler;
        if (handler == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context is null");
            throw null;
        }
        log("Calling onMiRilHookReady");
        Message messageObtain = Message.obtain();
        messageObtain.what = -1;
        this.mMtkHandler.sendMessage(messageObtain);
    }

    public MiRilHook(Context context, MiRilHookCallback miRilHookCallback, String str) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                MiRilHook.log("handleMessage msg id: " + message.what);
                new Bundle();
                message.getData();
                AsyncResult asyncResult = (AsyncResult) message.obj;
                switch (message.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(asyncResult);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(asyncResult);
                        break;
                }
            }
        };
        this.mNl1ServCellInfoSub0 = new Nl1ServingCellMeasurement(this);
        this.mNl1ServCellInfoSub1 = new Nl1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub0 = new El1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub1 = new El1ServingCellMeasurement(this);
        this.mEl1PuschPowerControlSub0 = new El1PuschPowerControl(this);
        this.mEl1PuschPowerControlSub1 = new El1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub0 = new Nl1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub1 = new Nl1PuschPowerControl(this);
        log("For Build, MiRilHook, context = " + context + ", cb = " + miRilHookCallback + ", packageName = " + str);
    }

    public MiRilHook(Context context, MiRilHookCallback miRilHookCallback) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                MiRilHook.log("handleMessage msg id: " + message.what);
                new Bundle();
                message.getData();
                AsyncResult asyncResult = (AsyncResult) message.obj;
                switch (message.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(asyncResult);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(asyncResult);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(asyncResult);
                        break;
                }
            }
        };
        this.mNl1ServCellInfoSub0 = new Nl1ServingCellMeasurement(this);
        this.mNl1ServCellInfoSub1 = new Nl1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub0 = new El1ServingCellMeasurement(this);
        this.mEl1ServCellInfoSub1 = new El1ServingCellMeasurement(this);
        this.mEl1PuschPowerControlSub0 = new El1PuschPowerControl(this);
        this.mEl1PuschPowerControlSub1 = new El1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub0 = new Nl1PuschPowerControl(this);
        this.mNl1PuschPowerControlSub1 = new Nl1PuschPowerControl(this);
        log("For Build, MiRilHook, context = " + context + ", cb = " + miRilHookCallback);
    }

    public MtkGsmCdmaPhone getPhone(int i) {
        return PhoneFactory.getPhone(i);
    }

    public void onSendAtCommandStrings(int i, String[] strArr, Message message) {
        MtkGsmCdmaPhone phone = getPhone(i);
        if (phone != null) {
            phone.invokeOemRilRequestStrings(strArr, message);
        }
    }

    private void onSendAtCommandRaw(int i, byte[] bArr, Message message) {
        MtkGsmCdmaPhone phone = getPhone(i);
        if (phone != null) {
            phone.invokeOemRilRequestRaw(bArr, message);
        }
    }

    private void rebootModem() {
        MtkGsmCdmaPhone phone = getPhone(0);
        if (phone != null) {
            phone.rebootModem((Message) null);
        }
    }

    class Nl1ServingCellMeasurement {
        long version = 65535;
        long pci = 65535;
        long narfcn = 65535;
        long band = 65535;
        int rsrp = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrp_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrp_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        long ssb_scs = 65535;
        long cur_time = 65535;

        public int dGetValueNums() {
            return 18;
        }

        Nl1ServingCellMeasurement(MiRilHook miRilHook) {
        }

        public Boolean dSetValues(String[] strArr) {
            MiRilHook.log("dSetValues");
            if (strArr == null) {
                MiRilHook.log("allData is null,return");
                return Boolean.FALSE;
            }
            MiRilHook.log("allData.length = " + strArr.length);
            if (strArr.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return Boolean.FALSE;
            }
            this.version = Long.parseLong(strArr[2]);
            this.pci = Long.parseLong(strArr[3]);
            this.narfcn = Long.parseLong(strArr[4]);
            this.band = Long.parseLong(strArr[5]);
            this.rsrp = Integer.parseInt(strArr[6]);
            this.rsrp_rx0 = Integer.parseInt(strArr[7]);
            this.rsrp_rx1 = Integer.parseInt(strArr[8]);
            this.rsrq = Integer.parseInt(strArr[9]);
            this.rsrq_rx0 = Integer.parseInt(strArr[10]);
            this.rsrq_rx1 = Integer.parseInt(strArr[11]);
            this.rssi = Integer.parseInt(strArr[12]);
            this.rssi_rx0 = Integer.parseInt(strArr[13]);
            this.rssi_rx1 = Integer.parseInt(strArr[14]);
            this.sinr = Integer.parseInt(strArr[15]);
            this.sinr_rx0 = Integer.parseInt(strArr[16]);
            this.sinr_rx1 = Integer.parseInt(strArr[17]);
            this.ssb_scs = Long.parseLong(strArr[18]);
            this.cur_time = Long.parseLong(strArr[19]);
            return Boolean.TRUE;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = 65535L;
            this.pci = 65535L;
            this.narfcn = 65535L;
            this.band = 65535L;
            this.rsrp = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrp_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrp_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.ssb_scs = 65535L;
            this.cur_time = 65535L;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate((dGetValueNums() + 6) * 4);
            byteBufferAllocate.putLong(this.version);
            byteBufferAllocate.putLong(this.pci);
            byteBufferAllocate.putLong(this.narfcn);
            byteBufferAllocate.putLong(this.band);
            byteBufferAllocate.putInt(this.rsrp);
            byteBufferAllocate.putInt(this.rsrp_rx0);
            byteBufferAllocate.putInt(this.rsrp_rx1);
            byteBufferAllocate.putInt(this.rsrq);
            byteBufferAllocate.putInt(this.rsrq_rx0);
            byteBufferAllocate.putInt(this.rsrq_rx1);
            byteBufferAllocate.putInt(this.rssi);
            byteBufferAllocate.putInt(this.rssi_rx0);
            byteBufferAllocate.putInt(this.rssi_rx1);
            byteBufferAllocate.putInt(this.sinr);
            byteBufferAllocate.putInt(this.sinr_rx0);
            byteBufferAllocate.putInt(this.sinr_rx1);
            byteBufferAllocate.putLong(this.ssb_scs);
            byteBufferAllocate.putLong(this.cur_time);
            byteBufferAllocate.flip();
            dClearValues();
            return byteBufferAllocate;
        }
    }

    public ByteBuffer onMtkGetNl1ServingCellMeasByAT(int i) {
        log("onMtkGetNl1ServingCellMeasByAT, subId = " + i);
        if (i != 0 && 1 != i) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(i, onPkDmfMsgGetByAT(i, 102, 20066), this.mHandler.obtainMessage(102));
        if (i == 0) {
            return this.mNl1ServCellInfoSub0.dGetByteBuffer();
        }
        return this.mNl1ServCellInfoSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateNl1ServingCellMeasDataByAT(AsyncResult asyncResult) {
        Object obj;
        log("onUpdateNl1ServingCellMeasDataByAT");
        if (asyncResult != null) {
            try {
                if (asyncResult.exception != null || (obj = asyncResult.result) == null) {
                    return;
                }
                String[] strArrSplit = ((String[]) obj)[0].split(",");
                log("allData.length = " + strArrSplit.length);
                if (strArrSplit.length != this.mNl1ServCellInfoSub0.dGetValueNums() + 2) {
                    log("response error");
                }
                if (Integer.parseInt(strArrSplit[1]) == 0) {
                    try {
                        if (true == this.mNl1ServCellInfoSub0.dSetValues(strArrSplit).booleanValue()) {
                            log("mNl1ServCellInfoSub0 setValues success");
                        } else {
                            log("mNl1ServCellInfoSub0 setValues failed");
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        log("mNl1ServCellInfoSub0 throw excepiton");
                        return;
                    }
                }
                try {
                    if (true == this.mNl1ServCellInfoSub1.dSetValues(strArrSplit).booleanValue()) {
                        log("mNl1ServCellInfoSub1 setValues success");
                    } else {
                        log("mNl1ServCellInfoSub1 setValues failed");
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log("mNl1ServCellInfoSub1 throw excepiton");
                    return;
                }
                log("Exception e :" + e);
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    class El1ServingCellMeasurement {
        int version = MiRilHook.MODEM_STATS_DATA_INVALID;
        long pci = 65535;
        long earfcn = 65535;
        int is_rrc_idle_mode = MiRilHook.MODEM_STATS_DATA_INVALID;
        int priority_cell = MiRilHook.MODEM_STATS_DATA_INVALID;
        int ant_mask_0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int ant_mask_1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int cell_measurement_bw = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrp = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrp_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrp_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rsrq_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int rssi_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
        int sinr_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
        long cur_time = 65535;

        public int dGetValueNums() {
            return 21;
        }

        El1ServingCellMeasurement(MiRilHook miRilHook) {
        }

        public Boolean dSetValues(String[] strArr) {
            MiRilHook.log("dSetValues");
            if (strArr == null) {
                MiRilHook.log("allData is null,return");
                return Boolean.FALSE;
            }
            MiRilHook.log("allData.length = " + strArr.length);
            if (strArr.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return Boolean.FALSE;
            }
            this.version = Integer.parseInt(strArr[2]);
            this.pci = Long.parseLong(strArr[3]);
            this.earfcn = Long.parseLong(strArr[4]);
            this.is_rrc_idle_mode = Integer.parseInt(strArr[5]);
            this.priority_cell = Integer.parseInt(strArr[6]);
            this.ant_mask_0 = Integer.parseInt(strArr[7]);
            this.ant_mask_1 = Integer.parseInt(strArr[8]);
            this.cell_measurement_bw = Integer.parseInt(strArr[9]);
            this.rsrp = Integer.parseInt(strArr[10]);
            this.rsrp_rx0 = Integer.parseInt(strArr[11]);
            this.rsrp_rx1 = Integer.parseInt(strArr[12]);
            this.rsrq = Integer.parseInt(strArr[13]);
            this.rsrq_rx0 = Integer.parseInt(strArr[14]);
            this.rsrq_rx1 = Integer.parseInt(strArr[15]);
            this.rssi = Integer.parseInt(strArr[16]);
            this.rssi_rx0 = Integer.parseInt(strArr[17]);
            this.rssi_rx1 = Integer.parseInt(strArr[18]);
            this.sinr = Integer.parseInt(strArr[19]);
            this.sinr_rx0 = Integer.parseInt(strArr[20]);
            this.sinr_rx1 = Integer.parseInt(strArr[21]);
            this.cur_time = Long.parseLong(strArr[22]);
            return Boolean.TRUE;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.pci = 65535L;
            this.earfcn = 65535L;
            this.is_rrc_idle_mode = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.priority_cell = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.ant_mask_0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.ant_mask_1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.cell_measurement_bw = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrp = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrp_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrp_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rsrq_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.rssi_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr_rx0 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.sinr_rx1 = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.cur_time = 65535L;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate((dGetValueNums() + 3) * 4);
            byteBufferAllocate.putInt(this.version);
            byteBufferAllocate.putLong(this.pci);
            byteBufferAllocate.putLong(this.earfcn);
            byteBufferAllocate.putInt(this.is_rrc_idle_mode);
            byteBufferAllocate.putInt(this.priority_cell);
            byteBufferAllocate.putInt(this.ant_mask_0);
            byteBufferAllocate.putInt(this.ant_mask_1);
            byteBufferAllocate.putInt(this.cell_measurement_bw);
            byteBufferAllocate.putInt(this.rsrp);
            byteBufferAllocate.putInt(this.rsrp_rx0);
            byteBufferAllocate.putInt(this.rsrp_rx1);
            byteBufferAllocate.putInt(this.rsrq);
            byteBufferAllocate.putInt(this.rsrq_rx0);
            byteBufferAllocate.putInt(this.rsrq_rx1);
            byteBufferAllocate.putInt(this.rssi);
            byteBufferAllocate.putInt(this.rssi_rx0);
            byteBufferAllocate.putInt(this.rssi_rx1);
            byteBufferAllocate.putInt(this.sinr);
            byteBufferAllocate.putInt(this.sinr_rx0);
            byteBufferAllocate.putInt(this.sinr_rx1);
            byteBufferAllocate.putLong(this.cur_time);
            byteBufferAllocate.flip();
            dClearValues();
            return byteBufferAllocate;
        }
    }

    public ByteBuffer onMtkGetEl1ServingCellMeasByAT(int i) {
        log("onMtkGetEl1ServingCellMeasByAT, subId = " + i);
        if (i != 0 && 1 != i) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(i, onPkDmfMsgGetByAT(i, 103, 20066), this.mHandler.obtainMessage(103));
        if (i == 0) {
            return this.mEl1ServCellInfoSub0.dGetByteBuffer();
        }
        return this.mEl1ServCellInfoSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateEl1ServingCellMeasByAT(AsyncResult asyncResult) {
        Object obj;
        log("onUpdateEl1ServingCellMeasByAT");
        if (asyncResult != null) {
            try {
                if (asyncResult.exception != null || (obj = asyncResult.result) == null) {
                    return;
                }
                String[] strArrSplit = ((String[]) obj)[0].split(",");
                log("allData.length = " + strArrSplit.length);
                if (strArrSplit.length != this.mEl1ServCellInfoSub0.dGetValueNums() + 2) {
                    log("response error");
                }
                if (Integer.parseInt(strArrSplit[1]) == 0) {
                    try {
                        if (true == this.mEl1ServCellInfoSub0.dSetValues(strArrSplit).booleanValue()) {
                            log("mEl1ServCellInfoSub0 setValues success");
                        } else {
                            log("mEl1ServCellInfoSub0 setValues failed");
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        log("mEl1ServCellInfoSub0 throw excepiton");
                        return;
                    }
                }
                try {
                    if (true == this.mEl1ServCellInfoSub1.dSetValues(strArrSplit).booleanValue()) {
                        log("mEl1ServCellInfoSub1 setValues success");
                    } else {
                        log("mEl1ServCellInfoSub1 setValues failed");
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log("mEl1ServCellInfoSub1 throw excepiton");
                    return;
                }
                log("Exception e :" + e);
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    class El1PuschPowerControl {
        int version = MiRilHook.MODEM_STATS_DATA_INVALID;
        int power = MiRilHook.MODEM_STATS_DATA_INVALID;

        public int dGetValueNums() {
            return 2;
        }

        El1PuschPowerControl(MiRilHook miRilHook) {
        }

        public Boolean dSetValues(String[] strArr) {
            MiRilHook.log("dSetValues");
            if (strArr == null) {
                MiRilHook.log("allData is null,return");
                return Boolean.FALSE;
            }
            MiRilHook.log("allData.length = " + strArr.length);
            if (strArr.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return Boolean.FALSE;
            }
            this.version = Integer.parseInt(strArr[2]);
            this.power = Integer.parseInt(strArr[3]);
            return Boolean.TRUE;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.power = MiRilHook.MODEM_STATS_DATA_INVALID;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(dGetValueNums() * 4);
            byteBufferAllocate.putInt(this.version);
            byteBufferAllocate.putInt(this.power);
            byteBufferAllocate.flip();
            dClearValues();
            return byteBufferAllocate;
        }
    }

    public ByteBuffer onMtkGetEl1PuschPowerControlByAT(int i) {
        log("onMtkGetEl1PuschPowerControlByAT, subId = " + i);
        if (i != 0 && 1 != i) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(i, onPkDmfMsgGetByAT(i, HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, 20066), this.mHandler.obtainMessage(HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT));
        if (i == 0) {
            return this.mEl1PuschPowerControlSub0.dGetByteBuffer();
        }
        return this.mEl1PuschPowerControlSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateEl1PuschPowerControlByAT(AsyncResult asyncResult) {
        Object obj;
        log("onUpdateEl1PuschPowerControlByAT");
        if (asyncResult != null) {
            try {
                if (asyncResult.exception != null || (obj = asyncResult.result) == null) {
                    return;
                }
                String[] strArrSplit = ((String[]) obj)[0].split(",");
                log("allData.length = " + strArrSplit.length);
                if (strArrSplit.length != this.mEl1PuschPowerControlSub0.dGetValueNums() + 2) {
                    log("response error");
                }
                if (Integer.parseInt(strArrSplit[1]) == 0) {
                    try {
                        if (true == this.mEl1PuschPowerControlSub0.dSetValues(strArrSplit).booleanValue()) {
                            log("mEl1PuschPowerControlSub0 setValues success");
                        } else {
                            log("mEl1PuschPowerControlSub0 setValues failed");
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        log("mEl1PuschPowerControlSub0 throw excepiton");
                        return;
                    }
                }
                try {
                    if (true == this.mEl1PuschPowerControlSub1.dSetValues(strArrSplit).booleanValue()) {
                        log("mEl1PuschPowerControlSub1 setValues success");
                    } else {
                        log("mEl1PuschPowerControlSub1 setValues failed");
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log("mEl1PuschPowerControlSub1 throw excepiton");
                    return;
                }
                log("Exception e :" + e);
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    class Nl1PuschPowerControl {
        int version = MiRilHook.MODEM_STATS_DATA_INVALID;
        int power = MiRilHook.MODEM_STATS_DATA_INVALID;

        public int dGetValueNums() {
            return 2;
        }

        Nl1PuschPowerControl(MiRilHook miRilHook) {
        }

        public Boolean dSetValues(String[] strArr) {
            MiRilHook.log("dSetValues");
            if (strArr == null) {
                MiRilHook.log("allData is null,return");
                return Boolean.FALSE;
            }
            MiRilHook.log("allData.length = " + strArr.length);
            if (strArr.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return Boolean.FALSE;
            }
            this.version = Integer.parseInt(strArr[2]);
            this.power = Integer.parseInt(strArr[3]);
            return Boolean.TRUE;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = MiRilHook.MODEM_STATS_DATA_INVALID;
            this.power = MiRilHook.MODEM_STATS_DATA_INVALID;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(dGetValueNums() * 4);
            byteBufferAllocate.putInt(this.version);
            byteBufferAllocate.putInt(this.power);
            byteBufferAllocate.flip();
            dClearValues();
            return byteBufferAllocate;
        }
    }

    public ByteBuffer onMtkGetNl1PuschPowerControlByAT(int i) {
        log("onMtkGetNl1PuschPowerControlByAT, subId = " + i);
        if (i != 0 && 1 != i) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(i, onPkDmfMsgGetByAT(i, HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, 20066), this.mHandler.obtainMessage(HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT));
        if (i == 0) {
            return this.mNl1PuschPowerControlSub0.dGetByteBuffer();
        }
        return this.mNl1PuschPowerControlSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateNl1PuschPowerControlByAT(AsyncResult asyncResult) {
        Object obj;
        log("onUpdateNl1PuschPowerControlByAT");
        if (asyncResult != null) {
            try {
                if (asyncResult.exception != null || (obj = asyncResult.result) == null) {
                    return;
                }
                String[] strArrSplit = ((String[]) obj)[0].split(",");
                log("allData.length = " + strArrSplit.length);
                if (strArrSplit.length != this.mNl1PuschPowerControlSub0.dGetValueNums() + 2) {
                    log("response error");
                }
                if (Integer.parseInt(strArrSplit[1]) == 0) {
                    try {
                        if (true == this.mNl1PuschPowerControlSub0.dSetValues(strArrSplit).booleanValue()) {
                            log("mNl1PuschPowerControlSub0 setValues success");
                        } else {
                            log("mNl1PuschPowerControlSub0 setValues failed");
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        log("mNl1PuschPowerControlSub0 throw excepiton");
                        return;
                    }
                }
                try {
                    if (true == this.mNl1PuschPowerControlSub1.dSetValues(strArrSplit).booleanValue()) {
                        log("mNl1PuschPowerControlSub1 setValues success");
                    } else {
                        log("mNl1PuschPowerControlSub1 setValues failed");
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log("mNl1PuschPowerControlSub1 throw excepiton");
                    return;
                }
                log("Exception e :" + e);
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    public void onMtkDMFToolCmdSend(int i, int i2, int i3) {
        log("onMtkDMFToolCmdSend");
        if (this.mHandler == null) {
            log("mHandler is null");
        } else if (i != 0 && i != 1) {
            log("subId is invalid");
        } else {
            onSendAtCommandStrings(i, onPkDmfMsgGetByAT(i, i2, i3), this.mHandler.obtainMessage(i2));
        }
    }

    private String[] onPkDmfMsgGetByAT(int i, int i2, int i3) {
        log("onPkDmfMsgGetByAT, subId = " + i + ", eventId = " + i2);
        StringBuffer stringBuffer = new StringBuffer("AT+EDMFAPP=");
        stringBuffer.append(Integer.toString(i3));
        stringBuffer.append(",");
        stringBuffer.append(String.valueOf(i2));
        log("final data is " + stringBuffer.toString());
        return new String[]{stringBuffer.toString(), "+EDMFAPP"};
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onRegisterAtUrcInd(int i, int i2, Handler handler) {
        log("onRegisterAtUrcInd, soltId = " + i + ", eventUrc = " + i2);
        if (handler == null) {
            log("handler is null");
            return;
        }
        MtkTelephonyManagerEx mtkTelephonyManagerEx = this.mMtkTmEx;
        if (i == 2) {
            mtkTelephonyManagerEx.registerAtUrcInd(0, handler, i2);
            this.mMtkTmEx.registerAtUrcInd(1, handler, i2);
        } else {
            mtkTelephonyManagerEx.registerAtUrcInd(i, handler, i2);
        }
    }

    public void onMtkHookPropGetSync(String str, String str2, Handler handler) {
        log("onMtkHookPropGetSync, propName = " + str + ", defaultValue = " + str2);
        if (str == null) {
            log("propName is null");
            return;
        }
        if (str2 == null) {
            log("defaultValue is null");
        } else if (handler == null) {
            log("handler is null");
        } else {
            onSendAtCommandRaw(0, onPkHookPropGet(str, str2), handler.obtainMessage(1));
        }
    }

    private byte[] onPkHookPropGet(String str, String str2) {
        log("onPkHookPropGet, propName = " + str + ", defaultValue = " + str2);
        if (str == null) {
            log("propName is null");
            return null;
        }
        if (str2 == null) {
            log("defaultValue is null");
            return null;
        }
        byte[] bArrOnGetHookMsgBytes = onGetHookMsgBytes(27 + str.length() + str2.length());
        ByteBuffer byteBufferOnGetHookMsgBuffer = onGetHookMsgBuffer(bArrOnGetHookMsgBytes);
        if (byteBufferOnGetHookMsgBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return null;
        }
        byteBufferOnGetHookMsgBuffer.put(XIAOMI_MTK_HOOK.getBytes());
        byteBufferOnGetHookMsgBuffer.putInt(1);
        byteBufferOnGetHookMsgBuffer.putInt(str.length());
        byteBufferOnGetHookMsgBuffer.put(str.getBytes());
        byteBufferOnGetHookMsgBuffer.putInt(str2.length());
        byteBufferOnGetHookMsgBuffer.put(str2.getBytes());
        return bArrOnGetHookMsgBytes;
    }

    private ByteBuffer onHookSendSync(byte[] bArr) {
        int iInvokeOemRilRequestRaw;
        log("onHookSendSync");
        if (bArr == null) {
            log("requestBytes is null");
            return null;
        }
        byte[] bArr2 = new byte[MTK_RESPONSE_BYTE_SIZE];
        try {
            iInvokeOemRilRequestRaw = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(bArr, bArr2);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
            Log.d("Exception e :", "an error happend!!");
            iInvokeOemRilRequestRaw = 0;
        }
        log("length is :102400, retLength is :" + iInvokeOemRilRequestRaw);
        if (iInvokeOemRilRequestRaw < 0 || iInvokeOemRilRequestRaw > MTK_RESPONSE_BYTE_SIZE) {
            return null;
        }
        return ModemUtils.getBuffer(bArr2, iInvokeOemRilRequestRaw);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public ByteBuffer onHookPropSetSync(String str, String str2) {
        return onHookSendSync(onPkHookPropSet(str, str2));
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean propSet(String str, String str2) {
        ByteBuffer byteBufferOnHookPropSetSync = onHookPropSetSync(str, str2);
        if (byteBufferOnHookPropSetSync == null) {
            log("propSet, byteBuf is null");
            return false;
        }
        int i = byteBufferOnHookPropSetSync.getInt();
        log("propSet, propName = " + str + ", ret = " + i);
        if (i == 0) {
            return true;
        }
        log("propSet failed");
        return false;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String onHookPropGetSync(String str, String str2) {
        ByteBuffer byteBufferOnHookSendSync = onHookSendSync(onPkHookPropGet(str, str2));
        if (byteBufferOnHookSendSync == null) {
            log("Maybe not support this hook interface, or no permit for read this prop");
            return str2;
        }
        if (byteBufferOnHookSendSync.getInt() != 0) {
            log("onHookPropGetSync, Maybe your input defaultValue is empty");
            return str2;
        }
        log("propValueLen = " + byteBufferOnHookSendSync.getInt());
        return ModemUtils.byteBufferToString(byteBufferOnHookSendSync);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String propGet(String str, String str2) {
        String strOnHookPropGetSync = onHookPropGetSync(str, str2);
        log("propGet, propName = " + str + ", defaultValue = " + str2 + ", propValue = " + strOnHookPropGetSync);
        return strOnHookPropGetSync;
    }

    public void onMtkHookPropSetSync(String str, String str2, Handler handler) {
        log("onMtkHookPropSetSync, propName = " + str + ", value = " + str2);
        if (str == null) {
            log("propName is null");
            return;
        }
        if (str2 == null) {
            log("defaultValue is null");
        } else if (handler == null) {
            log("handler is null");
        } else {
            onSendAtCommandRaw(0, onPkHookPropSet(str, str2), handler.obtainMessage(2));
        }
    }

    private byte[] onPkHookPropSet(String str, String str2) {
        log("onPkHookPropSet, propName = " + str + ", value = " + str2);
        if (str == null) {
            log("propName is null");
            return null;
        }
        if (str2 == null) {
            log("value is null");
            return null;
        }
        byte[] bArrOnGetHookMsgBytes = onGetHookMsgBytes(27 + str.length() + str2.length());
        ByteBuffer byteBufferOnGetHookMsgBuffer = onGetHookMsgBuffer(bArrOnGetHookMsgBytes);
        if (byteBufferOnGetHookMsgBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return null;
        }
        byteBufferOnGetHookMsgBuffer.put(XIAOMI_MTK_HOOK.getBytes());
        byteBufferOnGetHookMsgBuffer.putInt(2);
        byteBufferOnGetHookMsgBuffer.putInt(str.length());
        byteBufferOnGetHookMsgBuffer.put(str.getBytes());
        byteBufferOnGetHookMsgBuffer.putInt(str2.length());
        byteBufferOnGetHookMsgBuffer.put(str2.getBytes());
        return bArrOnGetHookMsgBytes;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean modemSSR(int i) {
        log("modemSSR, type = " + i);
        rebootModem();
        return true;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String onSendAtCommandByMtkTelephonyManagerEx(String str) {
        int iInvokeOemRilRequestRaw;
        log("onSendAtCommandByMtkTelephonyManagerEx, atCmd= " + str);
        String str2 = "";
        if (str == null) {
            log("atCmd is null!!");
            return "";
        }
        byte[] bArr = new byte[2001];
        try {
            iInvokeOemRilRequestRaw = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(str), bArr);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
            Log.d("Exception e :", "an error happend!!");
            iInvokeOemRilRequestRaw = 0;
        }
        log("ret is :" + Integer.toString(iInvokeOemRilRequestRaw));
        if (iInvokeOemRilRequestRaw > 0) {
            log("ret>0");
            str2 = new String(bArr, 0, iInvokeOemRilRequestRaw);
        }
        if (str2.indexOf("+CME ERROR") != -1) {
            str2 = null;
        }
        log("AT return string is:" + str2);
        return str2;
    }

    private byte[] onGetByteCmd(String str) {
        log("onGetByteCmd, atCmd " + str);
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[length + 1];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        bArr[length] = 0;
        log("CMD construct over");
        return bArr;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendGroupSarDsiAtCommand(int[] iArr) {
        log("onMtkSendGroupSarDsiAtCommand...");
        if (iArr == null || iArr.length == 0) {
            log("dsiGroup is null or empty!!");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer("AT+EGMC=1,\"sar_scenario_all\",");
        for (int i = 0; i < iArr.length; i++) {
            stringBuffer.append(iArr[i]);
            if (i != iArr.length - 1) {
                stringBuffer.append(",");
            }
        }
        log("final data is " + stringBuffer.toString());
        try {
            MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(stringBuffer.toString()), new byte[2001]);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendTASarDsiAtCommand(int i) {
        log("onMtkSendTASarDsiAtCommand...");
        StringBuffer stringBuffer = new StringBuffer("AT+ERFIDX=1,");
        stringBuffer.append(String.valueOf(i));
        log("final data is " + stringBuffer.toString());
        try {
            MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(stringBuffer.toString()), new byte[2001]);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public int onMtkGetTASarDsiAtCommand() {
        int iInvokeOemRilRequestRaw;
        String str;
        log("onMtkGetTASarDsiAtCommand");
        byte[] bArr = new byte[2001];
        try {
            iInvokeOemRilRequestRaw = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd("AT+ERFIDX?"), bArr);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
            iInvokeOemRilRequestRaw = 0;
        }
        log("ret is :" + Integer.toString(iInvokeOemRilRequestRaw));
        if (iInvokeOemRilRequestRaw <= 0) {
            str = "";
        } else {
            log("ret>0");
            str = new String(bArr, 0, iInvokeOemRilRequestRaw);
        }
        if (str.indexOf("+CME ERROR") != -1) {
            str = null;
        }
        log("AT return string is:" + str);
        if (str == null) {
            return 255;
        }
        int iIndexOf = str.indexOf(58) + 1;
        if (iIndexOf > 0) {
            String[] strArrSplit = str.substring(iIndexOf).split(",");
            if (strArrSplit.length >= 2) {
                try {
                    String strTrim = strArrSplit[1].trim();
                    if (strTrim.contains("\n")) {
                        strTrim = strTrim.split("\n")[0].trim();
                    }
                    return Integer.parseInt(strTrim);
                } catch (NumberFormatException e2) {
                    log("Error parsing target value to integer: " + e2.getMessage());
                    return 255;
                }
            }
            log("Insufficient numbers in the response string.");
            return 255;
        }
        log("Colon not found in the response.");
        return 255;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendUACAtCommand(int i) {
        log("onMtkSendUACAtCommand...");
        StringBuffer stringBuffer = new StringBuffer("AT+ERFIDX=0,");
        stringBuffer.append(String.valueOf(i));
        log("final data is " + stringBuffer.toString());
        onSendAtCommandStrings(0, new String[]{stringBuffer.toString(), "+ERFIDX"}, null);
    }

    public void onMtkSendEsimsInfoAtCommand(int i, Handler handler, int i2) {
        log("onMtkSendEsimsInfoAtCommand, subId=" + i + ", eventId=" + i2);
        if (i != 0 && i != 1) {
            log("onMtkSendEsimsInfoAtCommand: invalid subId=" + i);
        } else {
            if (handler == null) {
                log("onMtkSendEsimsInfoAtCommand: handler is null");
                return;
            }
            try {
                log("Sending AT command: AT+ESIMINFO=0 for subId=" + i);
                onSendAtCommandStrings(i, new String[]{"AT+ESIMINFO=0", "+ESIMINFO"}, handler.obtainMessage(i2, i, 0));
            } catch (Exception e) {
                log("onMtkSendEsimsInfoAtCommand error: " + e.getMessage());
            }
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendImbalanceAtCommand(int i) {
        log("onMtkSendImbalanceAtCommand...");
        log("final data is ".concat("MIPC_SET_DYNAMIC_ANT_BIAS" + i));
        onSendAtCommandStrings(0, new String[]{"MIPC_SET_DYNAMIC_ANT_BIAS", String.valueOf(i)}, null);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendAtCommand(String str, Handler handler) {
        log("onMtkSendAtCommand:" + str);
        if (str == null) {
            log("atCmd is null");
            return;
        }
        if (handler == null) {
            log("handler is null");
            return;
        }
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[length + 1];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        bArr[length] = 0;
        onSendAtCommandRaw(0, bArr, handler.obtainMessage(3));
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean onMtkFeatureCtlCmn(int i, int i2, int i3) {
        log("onMtkFeatureCtlCmn, phoneId = " + i3 + ", ctlCode = " + i2 + ", featureType = " + i);
        if (i < 1000 || i >= 1200 || MiRilHookBase.mContext == null) {
            return false;
        }
        Intent intent = new Intent("com.android.phone.intent.action.ME_CLOUD_CONTROL");
        intent.putExtra("FEATURE_TYPE", i);
        intent.putExtra("CONTROL_CODE", i2);
        MiRilHookBase.mContext.sendBroadcast(intent);
        return true;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public byte[] onMtkFeatureQueryCmn(int i, int i2, int i3) {
        log("onMtkFeatureQueryCmn, phoneId = " + i3 + ", featureType = " + i + ", queryCode = " + i2);
        if (i < 1000 || i >= 1200) {
            return null;
        }
        return onMtkModemEnhanceFeatureQuery(i, i2, i3);
    }

    private byte[] onMtkModemEnhanceFeatureQuery(int i, int i2, int i3) {
        int i4;
        int i5;
        byte[] bArr = new byte[4];
        if (i >= 1000 && i < 1100) {
            try {
                String str = SystemProperties.get("persist.radio.me.fr" + (i - 1000), "null");
                if (str.equals("true")) {
                    i5 = 1;
                } else {
                    i5 = str.equals("false") ? 0 : 11;
                }
                createBufferWithNativeByteOrder(bArr).putInt(i5);
                return bArr;
            } catch (Exception e) {
                e.printStackTrace();
                return bArr;
            }
        }
        if (i >= 1100 && i < 1200) {
            try {
                String str2 = SystemProperties.get("persist.radio.me.para" + (i - 1100), "null");
                if (str2.equals("null")) {
                    i4 = Integer.MAX_VALUE;
                } else {
                    try {
                        i4 = Integer.parseInt(str2);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return bArr;
                    }
                }
                createBufferWithNativeByteOrder(bArr).putInt(i4);
                return bArr;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return bArr;
    }

    private byte[] onGetHookMsgBytes(int i) {
        log("onGetHookMsgBytes, msg_len = " + i);
        return new byte[i + 1];
    }

    private ByteBuffer onGetHookMsgBuffer(byte[] bArr) {
        return ModemUtils.getBuffer(bArr);
    }

    public static ByteBuffer createBufferWithNativeByteOrder(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void dispose() {
        log("MiRilHook dispose");
        if (MiRilHookBase.mContext != null) {
            Message messageObtain = Message.obtain();
            messageObtain.what = -2;
            this.mMtkHandler.sendMessage(messageObtain);
            MiRilHookBase.mContext = null;
            this.mMtkHandler = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("MiRilHook-MTK(" + ModemUtils.getCurProcessName(MiRilHookBase.mContext) + ")", str);
    }
}
