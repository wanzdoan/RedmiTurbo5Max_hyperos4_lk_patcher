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
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
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

    public MiRilHook(Context context, Handler handler) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MiRilHook.log("handleMessage msg id: " + msg.what);
                new Bundle();
                msg.getData();
                AsyncResult ar = (AsyncResult) msg.obj;
                switch (msg.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(ar);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(ar);
                        break;
                    case 104:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(ar);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(ar);
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
        mContext = context;
        log("MiRilHook, context = " + context + ", handler = " + handler);
        if (mContext == null) {
            throw new IllegalArgumentException("Context is null");
        }
        this.mMtkHandler = handler;
        if (handler == null) {
            throw new IllegalArgumentException("Context is null");
        }
        log("Calling onMiRilHookReady");
        Message msg = Message.obtain();
        msg.what = -1;
        this.mMtkHandler.sendMessage(msg);
    }

    public MiRilHook(Context context, MiRilHookCallback cb, String packageName) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MiRilHook.log("handleMessage msg id: " + msg.what);
                new Bundle();
                msg.getData();
                AsyncResult ar = (AsyncResult) msg.obj;
                switch (msg.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(ar);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(ar);
                        break;
                    case 104:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(ar);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(ar);
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
        log("For Build, MiRilHook, context = " + context + ", cb = " + cb + ", packageName = " + packageName);
    }

    public MiRilHook(Context context, MiRilHookCallback cb) {
        super(context);
        this.mMtkHandler = null;
        this.mMtkTmEx = MtkTelephonyManagerEx.getDefault();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.mirilhook.MiRilHook.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MiRilHook.log("handleMessage msg id: " + msg.what);
                new Bundle();
                msg.getData();
                AsyncResult ar = (AsyncResult) msg.obj;
                switch (msg.what) {
                    case 102:
                        MiRilHook.log("HOOK_ID_NL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateNl1ServingCellMeasDataByAT(ar);
                        break;
                    case 103:
                        MiRilHook.log("HOOK_ID_EL1_SERVING_CELL_MEASUREMENT_BY_AT");
                        MiRilHook.this.onUpdateEl1ServingCellMeasByAT(ar);
                        break;
                    case 104:
                        MiRilHook.log("HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateEl1PuschPowerControlByAT(ar);
                        break;
                    case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                        MiRilHook.log("HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT");
                        MiRilHook.this.onUpdateNl1PuschPowerControlByAT(ar);
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
        log("For Build, MiRilHook, context = " + context + ", cb = " + cb);
    }

    public static int getHookType() {
        return 1;
    }

    public MtkGsmCdmaPhone getPhone(int phoneId) {
        return PhoneFactory.getPhone(phoneId);
    }

    public void onSendAtCommandStrings(int slot, String[] cmds, Message resp) {
        MtkGsmCdmaPhone phone = getPhone(slot);
        if (phone != null) {
            phone.invokeOemRilRequestStrings(cmds, resp);
        }
    }

    private void onSendAtCommandRaw(int slot, byte[] cmds, Message resp) {
        MtkGsmCdmaPhone phone = getPhone(slot);
        if (phone != null) {
            phone.invokeOemRilRequestRaw(cmds, resp);
        }
    }

    private void rebootModem() {
        MtkGsmCdmaPhone phone = getPhone(0);
        if (phone != null) {
            phone.rebootModem((Message) null);
        }
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    class Nl1ServingCellMeasurement {
        long version = 65535;
        long pci = 65535;
        long narfcn = 65535;
        long band = 65535;
        int rsrp = 65535;
        int rsrp_rx0 = 65535;
        int rsrp_rx1 = 65535;
        int rsrq = 65535;
        int rsrq_rx0 = 65535;
        int rsrq_rx1 = 65535;
        int rssi = 65535;
        int rssi_rx0 = 65535;
        int rssi_rx1 = 65535;
        int sinr = 65535;
        int sinr_rx0 = 65535;
        int sinr_rx1 = 65535;
        long ssb_scs = 65535;
        long cur_time = 65535;

        Nl1ServingCellMeasurement(MiRilHook this$0) {
        }

        public int dGetValueNums() {
            return 18;
        }

        public Boolean dSetValues(String[] allData) {
            MiRilHook.log("dSetValues");
            if (allData == null) {
                MiRilHook.log("allData is null,return");
                return false;
            }
            MiRilHook.log("allData.length = " + allData.length);
            if (allData.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return false;
            }
            this.version = Long.parseLong(allData[2]);
            this.pci = Long.parseLong(allData[3]);
            this.narfcn = Long.parseLong(allData[4]);
            this.band = Long.parseLong(allData[5]);
            this.rsrp = Integer.parseInt(allData[6]);
            this.rsrp_rx0 = Integer.parseInt(allData[7]);
            this.rsrp_rx1 = Integer.parseInt(allData[8]);
            this.rsrq = Integer.parseInt(allData[9]);
            this.rsrq_rx0 = Integer.parseInt(allData[10]);
            this.rsrq_rx1 = Integer.parseInt(allData[11]);
            this.rssi = Integer.parseInt(allData[12]);
            this.rssi_rx0 = Integer.parseInt(allData[13]);
            this.rssi_rx1 = Integer.parseInt(allData[14]);
            this.sinr = Integer.parseInt(allData[15]);
            this.sinr_rx0 = Integer.parseInt(allData[16]);
            this.sinr_rx1 = Integer.parseInt(allData[17]);
            this.ssb_scs = Long.parseLong(allData[18]);
            this.cur_time = Long.parseLong(allData[19]);
            return true;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = 65535L;
            this.pci = 65535L;
            this.narfcn = 65535L;
            this.band = 65535L;
            this.rsrp = 65535;
            this.rsrp_rx0 = 65535;
            this.rsrp_rx1 = 65535;
            this.rsrq = 65535;
            this.rsrq_rx0 = 65535;
            this.rsrq_rx1 = 65535;
            this.rssi = 65535;
            this.rssi_rx0 = 65535;
            this.rssi_rx1 = 65535;
            this.sinr = 65535;
            this.sinr_rx0 = 65535;
            this.sinr_rx1 = 65535;
            this.ssb_scs = 65535L;
            this.cur_time = 65535L;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer dBuffer = ByteBuffer.allocate((dGetValueNums() + 6) * 4);
            dBuffer.putLong(this.version);
            dBuffer.putLong(this.pci);
            dBuffer.putLong(this.narfcn);
            dBuffer.putLong(this.band);
            dBuffer.putInt(this.rsrp);
            dBuffer.putInt(this.rsrp_rx0);
            dBuffer.putInt(this.rsrp_rx1);
            dBuffer.putInt(this.rsrq);
            dBuffer.putInt(this.rsrq_rx0);
            dBuffer.putInt(this.rsrq_rx1);
            dBuffer.putInt(this.rssi);
            dBuffer.putInt(this.rssi_rx0);
            dBuffer.putInt(this.rssi_rx1);
            dBuffer.putInt(this.sinr);
            dBuffer.putInt(this.sinr_rx0);
            dBuffer.putInt(this.sinr_rx1);
            dBuffer.putLong(this.ssb_scs);
            dBuffer.putLong(this.cur_time);
            dBuffer.flip();
            dClearValues();
            return dBuffer;
        }
    }

    public ByteBuffer onMtkGetNl1ServingCellMeasByAT(int subId) {
        log("onMtkGetNl1ServingCellMeasByAT, subId = " + subId);
        if (subId != 0 && 1 != subId) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(subId, onPkDmfMsgGetByAT(subId, 102, DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID), this.mHandler.obtainMessage(102));
        if (subId == 0) {
            return this.mNl1ServCellInfoSub0.dGetByteBuffer();
        }
        return this.mNl1ServCellInfoSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateNl1ServingCellMeasDataByAT(AsyncResult result) {
        log("onUpdateNl1ServingCellMeasDataByAT");
        if (result != null) {
            try {
                if (result.exception == null && result.result != null) {
                    String[] data = (String[]) result.result;
                    String[] allData = data[0].split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    log("allData.length = " + allData.length);
                    if (allData.length != this.mNl1ServCellInfoSub0.dGetValueNums() + 2) {
                        log("response error");
                    }
                    int subId = Integer.parseInt(allData[1]);
                    if (subId == 0) {
                        try {
                            if (true == this.mNl1ServCellInfoSub0.dSetValues(allData).booleanValue()) {
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
                        if (true == this.mNl1ServCellInfoSub1.dSetValues(allData).booleanValue()) {
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
                }
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    class El1ServingCellMeasurement {
        int version = 65535;
        long pci = 65535;
        long earfcn = 65535;
        int is_rrc_idle_mode = 65535;
        int priority_cell = 65535;
        int ant_mask_0 = 65535;
        int ant_mask_1 = 65535;
        int cell_measurement_bw = 65535;
        int rsrp = 65535;
        int rsrp_rx0 = 65535;
        int rsrp_rx1 = 65535;
        int rsrq = 65535;
        int rsrq_rx0 = 65535;
        int rsrq_rx1 = 65535;
        int rssi = 65535;
        int rssi_rx0 = 65535;
        int rssi_rx1 = 65535;
        int sinr = 65535;
        int sinr_rx0 = 65535;
        int sinr_rx1 = 65535;
        long cur_time = 65535;

        El1ServingCellMeasurement(MiRilHook this$0) {
        }

        public int dGetValueNums() {
            return 21;
        }

        public Boolean dSetValues(String[] allData) {
            MiRilHook.log("dSetValues");
            if (allData == null) {
                MiRilHook.log("allData is null,return");
                return false;
            }
            MiRilHook.log("allData.length = " + allData.length);
            if (allData.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return false;
            }
            this.version = Integer.parseInt(allData[2]);
            this.pci = Long.parseLong(allData[3]);
            this.earfcn = Long.parseLong(allData[4]);
            this.is_rrc_idle_mode = Integer.parseInt(allData[5]);
            this.priority_cell = Integer.parseInt(allData[6]);
            this.ant_mask_0 = Integer.parseInt(allData[7]);
            this.ant_mask_1 = Integer.parseInt(allData[8]);
            this.cell_measurement_bw = Integer.parseInt(allData[9]);
            this.rsrp = Integer.parseInt(allData[10]);
            this.rsrp_rx0 = Integer.parseInt(allData[11]);
            this.rsrp_rx1 = Integer.parseInt(allData[12]);
            this.rsrq = Integer.parseInt(allData[13]);
            this.rsrq_rx0 = Integer.parseInt(allData[14]);
            this.rsrq_rx1 = Integer.parseInt(allData[15]);
            this.rssi = Integer.parseInt(allData[16]);
            this.rssi_rx0 = Integer.parseInt(allData[17]);
            this.rssi_rx1 = Integer.parseInt(allData[18]);
            this.sinr = Integer.parseInt(allData[19]);
            this.sinr_rx0 = Integer.parseInt(allData[20]);
            this.sinr_rx1 = Integer.parseInt(allData[21]);
            this.cur_time = Long.parseLong(allData[22]);
            return true;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = 65535;
            this.pci = 65535L;
            this.earfcn = 65535L;
            this.is_rrc_idle_mode = 65535;
            this.priority_cell = 65535;
            this.ant_mask_0 = 65535;
            this.ant_mask_1 = 65535;
            this.cell_measurement_bw = 65535;
            this.rsrp = 65535;
            this.rsrp_rx0 = 65535;
            this.rsrp_rx1 = 65535;
            this.rsrq = 65535;
            this.rsrq_rx0 = 65535;
            this.rsrq_rx1 = 65535;
            this.rssi = 65535;
            this.rssi_rx0 = 65535;
            this.rssi_rx1 = 65535;
            this.sinr = 65535;
            this.sinr_rx0 = 65535;
            this.sinr_rx1 = 65535;
            this.cur_time = 65535L;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer dBuffer = ByteBuffer.allocate((dGetValueNums() + 3) * 4);
            dBuffer.putInt(this.version);
            dBuffer.putLong(this.pci);
            dBuffer.putLong(this.earfcn);
            dBuffer.putInt(this.is_rrc_idle_mode);
            dBuffer.putInt(this.priority_cell);
            dBuffer.putInt(this.ant_mask_0);
            dBuffer.putInt(this.ant_mask_1);
            dBuffer.putInt(this.cell_measurement_bw);
            dBuffer.putInt(this.rsrp);
            dBuffer.putInt(this.rsrp_rx0);
            dBuffer.putInt(this.rsrp_rx1);
            dBuffer.putInt(this.rsrq);
            dBuffer.putInt(this.rsrq_rx0);
            dBuffer.putInt(this.rsrq_rx1);
            dBuffer.putInt(this.rssi);
            dBuffer.putInt(this.rssi_rx0);
            dBuffer.putInt(this.rssi_rx1);
            dBuffer.putInt(this.sinr);
            dBuffer.putInt(this.sinr_rx0);
            dBuffer.putInt(this.sinr_rx1);
            dBuffer.putLong(this.cur_time);
            dBuffer.flip();
            dClearValues();
            return dBuffer;
        }
    }

    public ByteBuffer onMtkGetEl1ServingCellMeasByAT(int subId) {
        log("onMtkGetEl1ServingCellMeasByAT, subId = " + subId);
        if (subId != 0 && 1 != subId) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(subId, onPkDmfMsgGetByAT(subId, 103, DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID), this.mHandler.obtainMessage(103));
        if (subId == 0) {
            return this.mEl1ServCellInfoSub0.dGetByteBuffer();
        }
        return this.mEl1ServCellInfoSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateEl1ServingCellMeasByAT(AsyncResult result) {
        log("onUpdateEl1ServingCellMeasByAT");
        if (result != null) {
            try {
                if (result.exception == null && result.result != null) {
                    String[] data = (String[]) result.result;
                    String[] allData = data[0].split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    log("allData.length = " + allData.length);
                    if (allData.length != this.mEl1ServCellInfoSub0.dGetValueNums() + 2) {
                        log("response error");
                    }
                    int subId = Integer.parseInt(allData[1]);
                    if (subId == 0) {
                        try {
                            if (true == this.mEl1ServCellInfoSub0.dSetValues(allData).booleanValue()) {
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
                        if (true == this.mEl1ServCellInfoSub1.dSetValues(allData).booleanValue()) {
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
                }
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    class El1PuschPowerControl {
        int version = 65535;
        int power = 65535;

        El1PuschPowerControl(MiRilHook this$0) {
        }

        public int dGetValueNums() {
            return 2;
        }

        public Boolean dSetValues(String[] allData) {
            MiRilHook.log("dSetValues");
            if (allData == null) {
                MiRilHook.log("allData is null,return");
                return false;
            }
            MiRilHook.log("allData.length = " + allData.length);
            if (allData.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return false;
            }
            this.version = Integer.parseInt(allData[2]);
            this.power = Integer.parseInt(allData[3]);
            return true;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = 65535;
            this.power = 65535;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer dBuffer = ByteBuffer.allocate(dGetValueNums() * 4);
            dBuffer.putInt(this.version);
            dBuffer.putInt(this.power);
            dBuffer.flip();
            dClearValues();
            return dBuffer;
        }
    }

    public ByteBuffer onMtkGetEl1PuschPowerControlByAT(int subId) {
        log("onMtkGetEl1PuschPowerControlByAT, subId = " + subId);
        if (subId != 0 && 1 != subId) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(subId, onPkDmfMsgGetByAT(subId, 104, DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID), this.mHandler.obtainMessage(104));
        if (subId == 0) {
            return this.mEl1PuschPowerControlSub0.dGetByteBuffer();
        }
        return this.mEl1PuschPowerControlSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateEl1PuschPowerControlByAT(AsyncResult result) {
        log("onUpdateEl1PuschPowerControlByAT");
        if (result != null) {
            try {
                if (result.exception == null && result.result != null) {
                    String[] data = (String[]) result.result;
                    String[] allData = data[0].split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    log("allData.length = " + allData.length);
                    if (allData.length != this.mEl1PuschPowerControlSub0.dGetValueNums() + 2) {
                        log("response error");
                    }
                    int subId = Integer.parseInt(allData[1]);
                    if (subId == 0) {
                        try {
                            if (true == this.mEl1PuschPowerControlSub0.dSetValues(allData).booleanValue()) {
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
                        if (true == this.mEl1PuschPowerControlSub1.dSetValues(allData).booleanValue()) {
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
                }
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
    class Nl1PuschPowerControl {
        int version = 65535;
        int power = 65535;

        Nl1PuschPowerControl(MiRilHook this$0) {
        }

        public int dGetValueNums() {
            return 2;
        }

        public Boolean dSetValues(String[] allData) {
            MiRilHook.log("dSetValues");
            if (allData == null) {
                MiRilHook.log("allData is null,return");
                return false;
            }
            MiRilHook.log("allData.length = " + allData.length);
            if (allData.length != dGetValueNums() + 2) {
                MiRilHook.log("allData length is uncorrect");
                return false;
            }
            this.version = Integer.parseInt(allData[2]);
            this.power = Integer.parseInt(allData[3]);
            return true;
        }

        private void dClearValues() {
            MiRilHook.log("dClearValues");
            this.version = 65535;
            this.power = 65535;
        }

        public ByteBuffer dGetByteBuffer() {
            MiRilHook.log("dGetByteBuffer");
            ByteBuffer dBuffer = ByteBuffer.allocate(dGetValueNums() * 4);
            dBuffer.putInt(this.version);
            dBuffer.putInt(this.power);
            dBuffer.flip();
            dClearValues();
            return dBuffer;
        }
    }

    public ByteBuffer onMtkGetNl1PuschPowerControlByAT(int subId) {
        log("onMtkGetNl1PuschPowerControlByAT, subId = " + subId);
        if (subId != 0 && 1 != subId) {
            log("subId is invalid");
            return null;
        }
        if (this.mHandler == null) {
            log("mHandler is null");
            return null;
        }
        onSendAtCommandStrings(subId, onPkDmfMsgGetByAT(subId, HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID), this.mHandler.obtainMessage(HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT));
        if (subId == 0) {
            return this.mNl1PuschPowerControlSub0.dGetByteBuffer();
        }
        return this.mNl1PuschPowerControlSub1.dGetByteBuffer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUpdateNl1PuschPowerControlByAT(AsyncResult result) {
        log("onUpdateNl1PuschPowerControlByAT");
        if (result != null) {
            try {
                if (result.exception == null && result.result != null) {
                    String[] data = (String[]) result.result;
                    String[] allData = data[0].split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                    log("allData.length = " + allData.length);
                    if (allData.length != this.mNl1PuschPowerControlSub0.dGetValueNums() + 2) {
                        log("response error");
                    }
                    int subId = Integer.parseInt(allData[1]);
                    if (subId == 0) {
                        try {
                            if (true == this.mNl1PuschPowerControlSub0.dSetValues(allData).booleanValue()) {
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
                        if (true == this.mNl1PuschPowerControlSub1.dSetValues(allData).booleanValue()) {
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
                }
            } catch (Exception e3) {
                log("Exception e :" + e3);
            }
        }
    }

    public void onMtkDMFToolCmdSend(int subId, int eventId, int appId) {
        log("onMtkDMFToolCmdSend");
        if (this.mHandler == null) {
            log("mHandler is null");
        } else if (subId != 0 && subId != 1) {
            log("subId is invalid");
        } else {
            onSendAtCommandStrings(subId, onPkDmfMsgGetByAT(subId, eventId, appId), this.mHandler.obtainMessage(eventId));
        }
    }

    private String[] onPkDmfMsgGetByAT(int subId, int eventId, int appId) {
        log("onPkDmfMsgGetByAT, subId = " + subId + ", eventId = " + eventId);
        StringBuffer cmd = new StringBuffer();
        cmd.append("AT+EDMFAPP=");
        cmd.append(Integer.toString(appId));
        cmd.append(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        cmd.append(String.valueOf(eventId));
        log("final data is " + cmd.toString());
        String[] atcmds = {cmd.toString(), "+EDMFAPP"};
        return atcmds;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onRegisterAtUrcInd(int soltId, int eventUrc, Handler handler) {
        log("onRegisterAtUrcInd, soltId = " + soltId + ", eventUrc = " + eventUrc);
        if (handler == null) {
            log("handler is null");
            return;
        }
        MtkTelephonyManagerEx mtkTelephonyManagerEx = this.mMtkTmEx;
        if (soltId == 2) {
            mtkTelephonyManagerEx.registerAtUrcInd(0, handler, eventUrc);
            this.mMtkTmEx.registerAtUrcInd(1, handler, eventUrc);
        } else {
            mtkTelephonyManagerEx.registerAtUrcInd(soltId, handler, eventUrc);
        }
    }

    public void onMtkHookPropGetSync(String propName, String defaultValue, Handler handler) {
        log("onMtkHookPropGetSync, propName = " + propName + ", defaultValue = " + defaultValue);
        if (propName == null) {
            log("propName is null");
            return;
        }
        if (defaultValue == null) {
            log("defaultValue is null");
        } else if (handler == null) {
            log("handler is null");
        } else {
            onSendAtCommandRaw(0, onPkHookPropGet(propName, defaultValue), handler.obtainMessage(1));
        }
    }

    private byte[] onPkHookPropGet(String propName, String defaultValue) {
        log("onPkHookPropGet, propName = " + propName + ", defaultValue = " + defaultValue);
        if (propName == null) {
            log("propName is null");
            return null;
        }
        if (defaultValue == null) {
            log("defaultValue is null");
            return null;
        }
        int msg_len = XIAOMI_MTK_HOOK.length() + 4 + 4 + 4 + propName.length() + defaultValue.length();
        byte[] hookBytes = onGetHookMsgBytes(msg_len);
        ByteBuffer reqBuffer = onGetHookMsgBuffer(hookBytes);
        if (reqBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return null;
        }
        reqBuffer.put(XIAOMI_MTK_HOOK.getBytes());
        reqBuffer.putInt(1);
        reqBuffer.putInt(propName.length());
        reqBuffer.put(propName.getBytes());
        reqBuffer.putInt(defaultValue.length());
        reqBuffer.put(defaultValue.getBytes());
        return hookBytes;
    }

    private ByteBuffer onHookSendSync(byte[] requestBytes) {
        log("onHookSendSync");
        if (requestBytes == null) {
            log("requestBytes is null");
            return null;
        }
        byte[] respByte = new byte[MTK_RESPONSE_BYTE_SIZE];
        int retLength = 0;
        try {
            retLength = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(requestBytes, respByte);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
            Log.d("Exception e :", "an error happend!!");
        }
        int length = respByte.length;
        log("length is :" + length + ", retLength is :" + retLength);
        if (retLength < 0 || retLength > respByte.length) {
            return null;
        }
        return ModemUtils.getBuffer(respByte, retLength);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public ByteBuffer onHookPropSetSync(String propName, String value) {
        return onHookSendSync(onPkHookPropSet(propName, value));
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean propSet(String propName, String value) {
        ByteBuffer byteBuf = onHookPropSetSync(propName, value);
        if (byteBuf == null) {
            log("propSet, byteBuf is null");
            return false;
        }
        int ret = byteBuf.getInt();
        log("propSet, propName = " + propName + ", ret = " + ret);
        if (ret != 0) {
            log("propSet failed");
            return false;
        }
        return true;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String onHookPropGetSync(String propName, String defaultValue) {
        ByteBuffer buf = onHookSendSync(onPkHookPropGet(propName, defaultValue));
        if (buf == null) {
            log("Maybe not support this hook interface, or no permit for read this prop");
            return defaultValue;
        }
        int ret = buf.getInt();
        if (ret != 0) {
            log("onHookPropGetSync, Maybe your input defaultValue is empty");
            return defaultValue;
        }
        int propValueLen = buf.getInt();
        log("propValueLen = " + propValueLen);
        return ModemUtils.byteBufferToString(buf);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String propGet(String propName, String defaultValue) {
        String propValue = onHookPropGetSync(propName, defaultValue);
        log("propGet, propName = " + propName + ", defaultValue = " + defaultValue + ", propValue = " + propValue);
        return propValue;
    }

    public void onMtkHookPropSetSync(String propName, String value, Handler handler) {
        log("onMtkHookPropSetSync, propName = " + propName + ", value = " + value);
        if (propName == null) {
            log("propName is null");
            return;
        }
        if (value == null) {
            log("defaultValue is null");
        } else if (handler == null) {
            log("handler is null");
        } else {
            onSendAtCommandRaw(0, onPkHookPropSet(propName, value), handler.obtainMessage(2));
        }
    }

    private byte[] onPkHookPropSet(String propName, String value) {
        log("onPkHookPropSet, propName = " + propName + ", value = " + value);
        if (propName == null) {
            log("propName is null");
            return null;
        }
        if (value == null) {
            log("value is null");
            return null;
        }
        int msg_len = XIAOMI_MTK_HOOK.length() + 4 + 4 + 4 + propName.length() + value.length();
        byte[] hookBytes = onGetHookMsgBytes(msg_len);
        ByteBuffer reqBuffer = onGetHookMsgBuffer(hookBytes);
        if (reqBuffer == null) {
            log("reqBuffer is null, will not send this hook msg");
            return null;
        }
        reqBuffer.put(XIAOMI_MTK_HOOK.getBytes());
        reqBuffer.putInt(2);
        reqBuffer.putInt(propName.length());
        reqBuffer.put(propName.getBytes());
        reqBuffer.putInt(value.length());
        reqBuffer.put(value.getBytes());
        return hookBytes;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean modemSSR(int type) {
        log("modemSSR, type = " + type);
        rebootModem();
        return true;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public String onSendAtCommandByMtkTelephonyManagerEx(String atCmd) {
        log("onSendAtCommandByMtkTelephonyManagerEx, atCmd= " + atCmd);
        String atCmdResult = "";
        if (atCmd == null) {
            log("atCmd is null!!");
            return "";
        }
        byte[] respByte = new byte[2001];
        int ret = 0;
        try {
            ret = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(atCmd), respByte);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
            Log.d("Exception e :", "an error happend!!");
        }
        log("ret is :" + Integer.toString(ret));
        if (ret > 0) {
            log("ret>0");
            atCmdResult = new String(respByte, 0, ret);
        }
        if (atCmdResult.indexOf("+CME ERROR") != -1) {
            atCmdResult = null;
        }
        log("AT return string is:" + atCmdResult);
        return atCmdResult;
    }

    private byte[] onGetByteCmd(String atCmd) {
        log("onGetByteCmd, atCmd " + atCmd);
        byte[] rawData = atCmd.getBytes();
        byte[] cmdByte = new byte[rawData.length + 1];
        System.arraycopy(rawData, 0, cmdByte, 0, rawData.length);
        cmdByte[cmdByte.length - 1] = 0;
        log("CMD construct over");
        return cmdByte;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendGroupSarDsiAtCommand(int[] dsiGroup) {
        log("onMtkSendGroupSarDsiAtCommand...");
        if (dsiGroup == null || dsiGroup.length == 0) {
            log("dsiGroup is null or empty!!");
            return;
        }
        StringBuffer cmd = new StringBuffer();
        cmd.append("AT+EGMC=1,\"sar_scenario_all\",");
        for (int i = 0; i < dsiGroup.length; i++) {
            cmd.append(dsiGroup[i]);
            if (i != dsiGroup.length - 1) {
                cmd.append(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            }
        }
        log("final data is " + cmd.toString());
        byte[] respByte = new byte[2001];
        try {
            MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(cmd.toString()), respByte);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendTASarDsiAtCommand(int dsi) {
        log("onMtkSendTASarDsiAtCommand...");
        StringBuffer cmd = new StringBuffer();
        cmd.append("AT+ERFIDX=1,");
        cmd.append(String.valueOf(dsi));
        log("final data is " + cmd.toString());
        byte[] respByte = new byte[2001];
        try {
            MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd(cmd.toString()), respByte);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public int onMtkGetTASarDsiAtCommand() {
        log("onMtkGetTASarDsiAtCommand");
        String atCmdResult = "";
        byte[] respByte = new byte[2001];
        int ret = 0;
        try {
            ret = MtkTelephonyManagerEx.getDefault().invokeOemRilRequestRaw(onGetByteCmd("AT+ERFIDX?"), respByte);
        } catch (Exception e) {
            log("load invokeOemRilRequestRaw failed, error:" + e.toString());
            e.printStackTrace();
        }
        log("ret is :" + Integer.toString(ret));
        if (ret > 0) {
            log("ret>0");
            atCmdResult = new String(respByte, 0, ret);
        }
        if (atCmdResult.indexOf("+CME ERROR") != -1) {
            atCmdResult = null;
        }
        log("AT return string is:" + atCmdResult);
        if (atCmdResult == null) {
            return ModemUtils.MODEM_STATS_DATA_INVALID_3;
        }
        int startIdx = atCmdResult.indexOf(58) + 1;
        if (startIdx > 0) {
            String numbersStr = atCmdResult.substring(startIdx);
            String[] numbersArray = numbersStr.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            if (numbersArray.length >= 2) {
                try {
                    String targetValueStr = numbersArray[1].trim();
                    if (targetValueStr.contains("\n")) {
                        String[] targetValueArray = targetValueStr.split("\n");
                        targetValueStr = targetValueArray[0].trim();
                    }
                    int dsi = Integer.parseInt(targetValueStr);
                    return dsi;
                } catch (NumberFormatException e2) {
                    log("Error parsing target value to integer: " + e2.getMessage());
                    return ModemUtils.MODEM_STATS_DATA_INVALID_3;
                }
            }
            log("Insufficient numbers in the response string.");
            return ModemUtils.MODEM_STATS_DATA_INVALID_3;
        }
        log("Colon not found in the response.");
        return ModemUtils.MODEM_STATS_DATA_INVALID_3;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendUACAtCommand(int scene) {
        log("onMtkSendUACAtCommand...");
        StringBuffer cmd = new StringBuffer();
        cmd.append("AT+ERFIDX=0,");
        cmd.append(String.valueOf(scene));
        log("final data is " + cmd.toString());
        String[] atcmds = {cmd.toString(), "+ERFIDX"};
        onSendAtCommandStrings(0, atcmds, null);
    }

    public void onMtkSendEsimsInfoAtCommand(int subId, Handler handler, int eventId) {
        log("onMtkSendEsimsInfoAtCommand, subId=" + subId + ", eventId=" + eventId);
        if (subId != 0 && subId != 1) {
            log("onMtkSendEsimsInfoAtCommand: invalid subId=" + subId);
            return;
        }
        if (handler == null) {
            log("onMtkSendEsimsInfoAtCommand: handler is null");
            return;
        }
        try {
            String[] atcmds = {"AT+ESIMINFO=0", "+ESIMINFO"};
            log("Sending AT command: AT+ESIMINFO=0 for subId=" + subId);
            onSendAtCommandStrings(subId, atcmds, handler.obtainMessage(eventId, subId, 0));
        } catch (Exception e) {
            log("onMtkSendEsimsInfoAtCommand error: " + e.getMessage());
        }
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendImbalanceAtCommand(int scene) {
        log("onMtkSendImbalanceAtCommand...");
        String cmd = "MIPC_SET_DYNAMIC_ANT_BIAS" + scene;
        log("final data is " + cmd);
        String[] atcmds = {"MIPC_SET_DYNAMIC_ANT_BIAS", String.valueOf(scene)};
        onSendAtCommandStrings(0, atcmds, null);
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void onMtkSendAtCommand(String atCmd, Handler handler) {
        log("onMtkSendAtCommand:" + atCmd);
        if (atCmd == null) {
            log("atCmd is null");
            return;
        }
        if (handler == null) {
            log("handler is null");
            return;
        }
        byte[] rawData = atCmd.getBytes();
        byte[] cmdByte = new byte[rawData.length + 1];
        System.arraycopy(rawData, 0, cmdByte, 0, rawData.length);
        cmdByte[cmdByte.length - 1] = 0;
        onSendAtCommandRaw(0, cmdByte, handler.obtainMessage(3));
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public boolean onMtkFeatureCtlCmn(int featureType, int ctlCode, int phoneId) {
        log("onMtkFeatureCtlCmn, phoneId = " + phoneId + ", ctlCode = " + ctlCode + ", featureType = " + featureType);
        if (featureType >= 1000 && featureType < 1200 && mContext != null) {
            Intent intent = new Intent("com.android.phone.intent.action.ME_CLOUD_CONTROL");
            intent.putExtra("FEATURE_TYPE", featureType);
            intent.putExtra("CONTROL_CODE", ctlCode);
            mContext.sendBroadcast(intent);
            return true;
        }
        return false;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public byte[] onMtkFeatureQueryCmn(int featureType, int queryCode, int phoneId) {
        log("onMtkFeatureQueryCmn, phoneId = " + phoneId + ", featureType = " + featureType + ", queryCode = " + queryCode);
        if (featureType >= 1000 && featureType < 1200) {
            byte[] result = onMtkModemEnhanceFeatureQuery(featureType, queryCode, phoneId);
            return result;
        }
        return null;
    }

    private byte[] onMtkModemEnhanceFeatureQuery(int featureType, int queryCode, int phoneId) {
        int propValue;
        int propValue2;
        byte[] result = new byte[4];
        if (featureType >= 1000 && featureType < 1100) {
            int featureId = featureType - 1000;
            String prop = "persist.radio.me.fr" + featureId;
            try {
                String featureStatus = SystemProperties.get(prop, "null");
                if (featureStatus.equals("true")) {
                    propValue2 = 1;
                } else if (featureStatus.equals(ModemUtils.PROP_BOOL_VALUE_OFF)) {
                    propValue2 = 0;
                } else {
                    propValue2 = 11;
                }
                ByteBuffer buffer = createBufferWithNativeByteOrder(result);
                buffer.putInt(propValue2);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        } else if (featureType >= 1100 && featureType < 1200) {
            int paraId = featureType - ModemUtils.XIAOMI_MODEM_HAL_CHIP_ID_MTK_ODM;
            String prop2 = "persist.radio.me.para" + paraId;
            try {
                String paraStatus = SystemProperties.get(prop2, "null");
                if (paraStatus.equals("null")) {
                    propValue = Integer.MAX_VALUE;
                } else {
                    try {
                        propValue = Integer.parseInt(paraStatus);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return result;
                    }
                }
                ByteBuffer buffer2 = createBufferWithNativeByteOrder(result);
                buffer2.putInt(propValue);
            } catch (Exception e3) {
                e3.printStackTrace();
                return result;
            }
        }
        return result;
    }

    private byte[] onGetHookMsgBytes(int msg_len) {
        log("onGetHookMsgBytes, msg_len = " + msg_len);
        byte[] hookBytes = new byte[msg_len + 1];
        return hookBytes;
    }

    private ByteBuffer onGetHookMsgBuffer(byte[] hookBytes) {
        return ModemUtils.getBuffer(hookBytes);
    }

    public static ByteBuffer createBufferWithNativeByteOrder(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        return buf;
    }

    @Override // com.xiaomi.mirilhook.MiRilHookBase
    public void dispose() {
        log("MiRilHook dispose");
        if (mContext != null) {
            Message msg = Message.obtain();
            msg.what = -2;
            this.mMtkHandler.sendMessage(msg);
            mContext = null;
            this.mMtkHandler = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        Log.d("MiRilHook-MTK(" + ModemUtils.getCurProcessName(mContext) + ")", msg);
    }
}
