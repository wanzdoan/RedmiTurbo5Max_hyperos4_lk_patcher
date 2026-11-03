package com.nxp.osu;

import com.nxp.id.tp.common.HexString;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class UAIQueryResponse {
    private static final byte[] prefixRsp = {-2, -127, -83, -33, 67, -127};
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

    private UAIQueryResponse() {
    }

    public static UAIQueryResponse parse(byte[] bArr) {
        short s = 3;
        while (true) {
            byte[] bArr2 = prefixRsp;
            if (s < ((short) bArr2.length)) {
                if (bArr2[s] != bArr[s]) {
                    return null;
                }
                s = (short) (s + 1);
            } else {
                UAIQueryResponse uAIQueryResponse = new UAIQueryResponse();
                uAIQueryResponse.isHostResumeSupported = false;
                short s2 = 7;
                while (s2 < bArr.length - 2) {
                    try {
                        short s3 = (short) (s2 + 1);
                        byte b = bArr[s2];
                        short s4 = (short) (s3 + 1);
                        byte b2 = bArr[s3];
                        switch (b) {
                            case -128:
                                uAIQueryResponse.V = bArr[s4];
                                break;
                            case -127:
                                uAIQueryResponse.CSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -126:
                                uAIQueryResponse.RSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -125:
                                uAIQueryResponse.FSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -124:
                                uAIQueryResponse.PSN = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -123:
                                uAIQueryResponse.UOS_ID = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -122:
                                System.arraycopy(bArr, s4, uAIQueryResponse.serialNo, 0, b2);
                                break;
                            case -121:
                                uAIQueryResponse.deviceType = bArr[s4];
                                break;
                            case -120:
                                System.arraycopy(bArr, s4, uAIQueryResponse.customerPubKeyID, 0, b2);
                                break;
                            case -119:
                                System.arraycopy(bArr, s4, uAIQueryResponse.signaturePubKeyID, 0, b2);
                                break;
                            case -118:
                                uAIQueryResponse.RM = bArr[s4];
                                break;
                            case -117:
                                uAIQueryResponse.state = (short) ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT));
                                break;
                            case -116:
                                System.arraycopy(bArr, s4, uAIQueryResponse.hash, 0, b2);
                                break;
                            case -115:
                                uAIQueryResponse.retryCounterOSU = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 65535;
                                break;
                            case -114:
                                uAIQueryResponse.retryCounterOSPatch = ((bArr[s4] << 8) + (bArr[s4 + 1] & TransitionInfo.INIT)) & 4095;
                                break;
                            case -113:
                            default:
                                return null;
                            case -112:
                                uAIQueryResponse.amdHState = bArr[s4];
                                uAIQueryResponse.isHostResumeSupported = true;
                                break;
                        }
                        s2 = (short) (s4 + b2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return uAIQueryResponse;
                    }
                }
                return uAIQueryResponse;
            }
        }
    }

    public String toString() {
        return "\n -------- OS Info -------\n".concat(String.format("Version Number     : 0x%02X\n", Byte.valueOf(this.V))).concat(String.format("Sequence Number    : 0x%04X\n", Integer.valueOf(this.CSN))).concat(String.format("Reference Sequence : 0x%04X\n", Integer.valueOf(this.RSN))).concat(String.format("Final Sequence     : 0x%04X\n", Integer.valueOf(this.FSN))).concat(String.format("Previous Sequence  : 0x%04X\n", Integer.valueOf(this.PSN))).concat(String.format("Device Type        : %d\n", Byte.valueOf(this.deviceType))).concat(String.format("Serial Number      : " + HexString.fromByteArray(this.serialNo, 0) + "\n", new Object[0])).concat(String.format("UOS ID             : 0x%04X\n", Integer.valueOf(this.UOS_ID))).concat(String.format("RM                 : 0x%04X\n", Byte.valueOf(this.RM))).concat(String.format("State              : 0x%04X\n", Short.valueOf(this.state))).concat(String.format("RETRY COUNTER      : %d\n", Integer.valueOf(this.retryCounterOSU))).concat(String.format("RETRY COUNTER Patch: %d\n", Integer.valueOf(this.retryCounterOSPatch)));
    }
}
