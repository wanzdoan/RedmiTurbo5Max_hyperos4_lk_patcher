package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1Oid extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(6);
    private String idString;

    public static Asn1Oid parse(HexString hexString) {
        return new Asn1Oid(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1Oid(HexString hexString) {
        super(TAG, hexString);
        this.idString = oidToString(hexString);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("OID (len=" + getValue().length() + ") [");
        sb.append(getValue());
        sb.append("/");
        sb.append(this.idString);
        sb.append("]");
        return sb.toString();
    }

    private static String oidToString(HexString hexString) {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        BigInteger bigIntegerShiftLeft = null;
        long j = 0;
        for (int i = 0; i < hexString.length(); i++) {
            byte bAt = hexString.at(i);
            if (j <= 72057594037927808L) {
                long j2 = j + ((long) (bAt & 127));
                if ((bAt & 128) == 0) {
                    if (z) {
                        if (j2 < 40) {
                            sb.append('0');
                        } else if (j2 < 80) {
                            sb.append('1');
                            j2 -= 40;
                        } else {
                            sb.append('2');
                            j2 -= 80;
                        }
                        z = false;
                    }
                    sb.append('.');
                    sb.append(j2);
                    j = 0;
                } else {
                    j = j2 << 7;
                }
            } else {
                if (bigIntegerShiftLeft == null) {
                    bigIntegerShiftLeft = BigInteger.valueOf(j);
                }
                BigInteger bigIntegerOr = bigIntegerShiftLeft.or(BigInteger.valueOf(bAt & 127));
                if ((bAt & 128) == 0) {
                    if (z) {
                        sb.append('2');
                        bigIntegerOr = bigIntegerOr.subtract(BigInteger.valueOf(80L));
                        z = false;
                    }
                    sb.append('.');
                    sb.append(bigIntegerOr);
                    bigIntegerShiftLeft = null;
                    j = 0;
                } else {
                    bigIntegerShiftLeft = bigIntegerOr.shiftLeft(7);
                }
            }
        }
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1Oid m4790clone() {
        return new Asn1Oid(getValue());
    }
}
