package com.nxp.id.jsbl.uai.api.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class APDUUAI {
    private byte cla;
    private byte[] data;
    private byte ins;
    private String lc;
    private byte p1;
    private byte p2;
    private byte sw1;
    private byte sw2;

    public enum osuType {
        OS_TYPE_REAL_UPDATE,
        OS_TYPE_SELF_UPDATE,
        OS_TYPE_REAL_UPDATE_WITH_AMDH,
        OS_TYPE_SELF_UPDATE_WITH_AMDH,
        OS_TYPE_PATCH_UPDATE,
        OS_TYPE_REAL_PATCH_UPDATE,
        OS_TYPE_UNKNOWN
    }

    public enum outputSyntax {
        APDU,
        JCSH
    }

    public APDUUAI(byte b, byte b2) {
        this.cla = b;
        this.ins = b2;
    }

    public static List byteArrayToUAIAPDU(byte[] bArr, outputSyntax outputsyntax, byte b, Integer num, boolean z) throws JSBLExceptionExternal {
        APDUUAI apduuai;
        Utils.checkParamNotNull(bArr);
        if (num == null) {
            num = 640;
        }
        if (b != -86 && b != -69) {
            throw new JSBLExceptionExternal("Only applies to UAI CCI and JCI");
        }
        if (b == -86) {
            apduuai = new APDUUAI(z ? (byte) 0 : (byte) -95, (byte) -86);
        } else {
            apduuai = new APDUUAI(z ? (byte) 0 : (byte) -95, (byte) -69);
        }
        LinkedList linkedList = new LinkedList();
        int iIntValue = num.intValue();
        byte b2 = 1;
        int i = 0;
        byte b3 = 0;
        while (b2 == 1) {
            if (num.intValue() + i >= bArr.length) {
                iIntValue = bArr.length - i;
                b2 = 0;
            }
            byte b4 = (byte) (b3 + 1);
            apduuai.setP(b3, b2);
            int i2 = i + iIntValue;
            apduuai.setData(Arrays.copyOfRange(bArr, i, i2));
            linkedList.add(apduuai.toString(outputsyntax));
            i = i2;
            b3 = b4;
        }
        return linkedList;
    }

    public static List byteArrayToUAIAPDU(byte[] bArr, outputSyntax outputsyntax, byte b, boolean z) {
        Utils.checkParamNotNull(bArr);
        return byteArrayToUAIAPDU(bArr, outputsyntax, b, 640, z);
    }

    public void setData(byte[] bArr) {
        setData(bArr, (byte) -112, (byte) 0);
    }

    public void setData(byte[] bArr, byte b, byte b2) {
        Utils.checkParamNotNull(bArr);
        this.lc = getLc(bArr.length);
        this.data = bArr;
        this.sw1 = b;
        this.sw2 = b2;
    }

    public void setP(byte b, byte b2) {
        this.p1 = b;
        this.p2 = b2;
    }

    private String getLc(int i) {
        byte[] bArr;
        String hexString = Integer.toHexString(i);
        if (hexString.length() <= 2) {
            bArr = new byte[]{Utils.hexStringToInteger(hexString).byteValue()};
        } else {
            byte[] bArr2 = new byte[3];
            byte[] byteArray = BigInteger.valueOf(Utils.hexStringToInteger(hexString).intValue()).toByteArray();
            System.arraycopy(byteArray, 0, bArr2, 3 - byteArray.length, byteArray.length);
            bArr = bArr2;
        }
        return Utils.byteArrayToHexString(bArr);
    }

    public String toString() {
        return toString(outputSyntax.JCSH);
    }

    public String toString(outputSyntax outputsyntax) {
        StringBuilder sb = new StringBuilder();
        outputSyntax outputsyntax2 = outputSyntax.JCSH;
        if (outputsyntax == outputsyntax2) {
            sb.append("/send ");
        }
        sb.append(Utils.byteToHexString(this.cla));
        sb.append(Utils.byteToHexString(this.ins));
        sb.append(Utils.byteToHexString(this.p1));
        sb.append(Utils.byteToHexString(this.p2));
        sb.append(this.lc);
        sb.append(Utils.byteArrayToHexString(this.data));
        if (outputsyntax == outputsyntax2) {
            sb.append(" ");
            sb.append(Utils.byteToHexString(this.sw1));
            sb.append(Utils.byteToHexString(this.sw2));
        }
        return sb.toString();
    }
}
