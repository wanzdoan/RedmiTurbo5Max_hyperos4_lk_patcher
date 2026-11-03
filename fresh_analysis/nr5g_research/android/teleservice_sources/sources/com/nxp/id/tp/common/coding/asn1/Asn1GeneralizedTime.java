package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1GeneralizedTime extends Asn1Date {
    public static final HexString TAG = HexString.fromInteger(24);
    private Date date;

    public static Asn1GeneralizedTime parse(HexString hexString) {
        return new Asn1GeneralizedTime(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1GeneralizedTime(HexString hexString) {
        super(TAG, hexString);
        try {
            this.date = new SimpleDateFormat("yyyyMMddHHmmss'Z'").parse(hexString.toAsciiString());
        } catch (ParseException e) {
            throw new InvalidAsn1CodingException("Date parse error", e);
        }
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("GENERALIZED TIME (len=" + getValue().length() + ") [");
        sb.append(new SimpleDateFormat("yyyyMMddHHmmss'Z'").format(this.date));
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1GeneralizedTime m4783clone() {
        return new Asn1GeneralizedTime(getValue());
    }
}
