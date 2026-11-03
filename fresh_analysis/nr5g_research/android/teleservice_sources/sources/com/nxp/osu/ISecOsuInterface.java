package com.nxp.osu;

/* JADX INFO: loaded from: classes.dex */
public class ISecOsuInterface {
    public ISecOsuAccess mAccess = null;
    public TERMINAL_TYPE mTerminal = TERMINAL_TYPE.SPI;
    public IModem mModem = null;

    public enum TERMINAL_TYPE {
        SPI(1),
        SMB(2),
        TERMINAL1(1),
        TERMINAL2(2),
        TERMINAL3(3);

        private int mTerminalId;

        TERMINAL_TYPE(int i) {
            this.mTerminalId = i;
        }

        public int getValue() {
            return this.mTerminalId;
        }
    }
}
