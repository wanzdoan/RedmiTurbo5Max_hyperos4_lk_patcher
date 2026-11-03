package com.android.internal.telephony.ntnphone;

import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class NtnAntConstants {
    public RxAntEnableType rx_ant_enable_type;
    public RxAntReportMode rx_ant_report_mode;
    public TxAntEnableType tx_ant_enable_type;

    public enum TxAntEnableType {
        TX_UNSET(-1),
        TX_ANT1(0),
        TX_ANT2(1);

        private int Type;

        TxAntEnableType(int i) {
            this.Type = i;
        }

        public int getType() {
            return this.Type;
        }
    }

    public enum RxAntEnableType {
        RX_UNSET(-1),
        RX_ANT1(0),
        RX_ANT2(1),
        RX_ANT_COMBINED(2);

        private int Type;

        RxAntEnableType(int i) {
            this.Type = i;
        }

        public int getType() {
            return this.Type;
        }
    }

    public enum RxAntReportMode {
        RX_CLOSE(0),
        RX_ANT1(1),
        RX_ANT2(2),
        RX_ANT_COMBINED(3);

        private int Mode;

        RxAntReportMode(int i) {
            this.Mode = i;
        }

        public int getMode() {
            return this.Mode;
        }
    }

    public TxAntEnableType TxAntEnableTypeFromRILInt(int i) {
        if (i == -1) {
            return TxAntEnableType.TX_UNSET;
        }
        if (i == 0) {
            return TxAntEnableType.TX_ANT1;
        }
        if (i == 1) {
            return TxAntEnableType.TX_ANT2;
        }
        TxAntEnableType txAntEnableType = TxAntEnableType.TX_UNSET;
        loge("newTxAntEnableType: bad type: " + i + " use TX_UNSET");
        return txAntEnableType;
    }

    public RxAntEnableType RxAntEnableTypeFromRILInt(int i) {
        if (i == -1) {
            return RxAntEnableType.RX_UNSET;
        }
        if (i == 0) {
            return RxAntEnableType.RX_ANT1;
        }
        if (i == 1) {
            return RxAntEnableType.RX_ANT2;
        }
        if (i == 2) {
            return RxAntEnableType.RX_ANT_COMBINED;
        }
        RxAntEnableType rxAntEnableType = RxAntEnableType.RX_UNSET;
        loge("newRxAntEnableType: bad type: " + i + " use RX_UNSET");
        return rxAntEnableType;
    }

    public RxAntReportMode RxAntReportModeFromRILInt(int i) {
        if (i == 0) {
            return RxAntReportMode.RX_CLOSE;
        }
        if (i == 1) {
            return RxAntReportMode.RX_ANT1;
        }
        if (i == 2) {
            return RxAntReportMode.RX_ANT2;
        }
        if (i == 3) {
            return RxAntReportMode.RX_ANT_COMBINED;
        }
        RxAntReportMode rxAntReportMode = RxAntReportMode.RX_CLOSE;
        loge("newRxAntReportMode: bad mode: " + i + " use RX_CLOSE");
        return rxAntReportMode;
    }

    public String toString() {
        return "{Tx=" + this.tx_ant_enable_type + ",Rx=" + this.rx_ant_enable_type + ",RxReportMode=" + this.rx_ant_report_mode + "}";
    }

    private void loge(String str) {
        Rlog.e("NtnAntConstants", str);
    }
}
