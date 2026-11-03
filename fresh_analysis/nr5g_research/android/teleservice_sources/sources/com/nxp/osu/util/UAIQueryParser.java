package com.nxp.osu.util;

import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class UAIQueryParser {
    private static final byte[] sPrefixRsp = {-2, -127, -83, -33, 67, -127};
    public int CSN;
    public int FSN;
    public int PSN;
    public byte RM;
    public int RSN;
    public int UOS_ID;
    public byte V;
    public byte deviceType;
    public int retryCounterOSU;
    public short state;
    public byte[] serialNo = new byte[24];
    public byte[] customerPubKeyID = new byte[32];
    public byte[] signaturePubKeyID = new byte[32];
    public byte[] hash = new byte[32];
    public int retryCounterOSPatch = 0;
    public short amdHState = 0;
    public boolean isHostResumeSupported = false;

    private UAIQueryParser() {
    }

    public static UAIQueryParser parse(byte[] bArr) {
        short s = 3;
        while (true) {
            byte[] bArr2 = sPrefixRsp;
            if (s < ((short) bArr2.length)) {
                if (bArr2[s] != bArr[s]) {
                    System.out.println("UAI: Invalid response prefix");
                    return null;
                }
                s = (short) (s + 1);
            } else {
                UAIQueryParser uAIQueryParser = new UAIQueryParser();
                uAIQueryParser.isHostResumeSupported = false;
                short s2 = 7;
                while (s2 < bArr.length - 2) {
                    short s3 = (short) (s2 + 1);
                    byte b = bArr[s2];
                    short s4 = (short) (s3 + 1);
                    byte b2 = bArr[s3];
                    switch (b) {
                        case -128:
                            uAIQueryParser.V = bArr[s4];
                            break;
                        case -127:
                            uAIQueryParser.CSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -126:
                            uAIQueryParser.RSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -125:
                            uAIQueryParser.FSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -124:
                            uAIQueryParser.PSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -123:
                            uAIQueryParser.UOS_ID = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -122:
                            System.arraycopy(bArr, s4, uAIQueryParser.serialNo, 0, b2);
                            break;
                        case -121:
                            uAIQueryParser.deviceType = bArr[s4];
                            break;
                        case -120:
                            System.arraycopy(bArr, s4, uAIQueryParser.customerPubKeyID, 0, b2);
                            break;
                        case -119:
                            System.arraycopy(bArr, s4, uAIQueryParser.signaturePubKeyID, 0, b2);
                            break;
                        case -118:
                            uAIQueryParser.RM = bArr[s4];
                            break;
                        case -117:
                            uAIQueryParser.state = (short) ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT));
                            break;
                        case -116:
                            System.arraycopy(bArr, s4, uAIQueryParser.hash, 0, b2);
                            break;
                        case -115:
                            uAIQueryParser.retryCounterOSU = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -114:
                            uAIQueryParser.retryCounterOSPatch = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                            break;
                        case -113:
                        default:
                            System.out.println("Unknown tag in query response");
                            return null;
                        case -112:
                            uAIQueryParser.amdHState = bArr[s4];
                            uAIQueryParser.isHostResumeSupported = true;
                            break;
                    }
                    s2 = (short) (s4 + b2);
                }
                return uAIQueryParser;
            }
        }
    }

    public String toString() {
        return "\nUAI Query Response\n".concat(String.format("V______________________: 0x%02X\n", Byte.valueOf(this.V))).concat(String.format("CSN____________________: 0x%04X\n", Integer.valueOf(this.CSN))).concat(String.format("RSN____________________: 0x%04X\n", Integer.valueOf(this.RSN))).concat(String.format("FSN____________________: 0x%04X\n", Integer.valueOf(this.FSN))).concat(String.format("PSN____________________: 0x%04X\n", Integer.valueOf(this.PSN))).concat(String.format("UOS ID_________________: %s\n", getOsID())).concat(String.format("Dev Type_______________: %X\n", Byte.valueOf(this.deviceType))).concat(String.format("RM_____________________: 0x%04X\n", Byte.valueOf(this.RM))).concat(String.format("State__________________: %s\n", getUAIState())).concat(String.format("RETRY COUNTER__________: %d\n", Integer.valueOf(this.retryCounterOSU))).concat(String.format("RETRY COUNTER__________: %d\n", Integer.valueOf(this.retryCounterOSPatch)));
    }

    private String getOsID() {
        int i = this.UOS_ID;
        if (1 == i) {
            return "OSU1 (0x01)";
        }
        if (2 == i) {
            return "OSU2 (0x02)";
        }
        if (17 == i) {
            return "OS SELF Update (0x11)";
        }
        if (90 == i) {
            return "JCOP OS (0x5A)";
        }
        return "";
    }

    private String getUAIState() {
        short s = this.state;
        if (23130 == s) {
            return "IDLE";
        }
        if (4369 == s) {
            return "CCI_VALIDATED";
        }
        if (8738 == s) {
            return "JCI VALIDATED";
        }
        if (-23131 == s) {
            return "UAI Error";
        }
        return "UAI Unknown state";
    }
}
