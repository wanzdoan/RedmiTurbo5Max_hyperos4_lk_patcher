package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.ASN1Constants$EUpdateScriptType;
import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1Enumerated;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import com.nxp.id.tp.common.coding.asn1.Asn1String;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class UpdateScript implements Asn1Encodable {
    private List apdus;
    private final ASN1Constants$EUpdateScriptType binaryType;
    private final String details;
    private int index;

    private UpdateScript(ASN1Constants$EUpdateScriptType aSN1Constants$EUpdateScriptType, String str, int i, List list) {
        this.binaryType = aSN1Constants$EUpdateScriptType;
        this.index = i;
        this.details = str;
        this.apdus = new LinkedList(list);
    }

    public static UpdateScript decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        ASN1Constants$EUpdateScriptType aSN1Constants$EUpdateScriptTypeFindImageBinaryType = findImageBinaryType(asn1Sequence.get(0).getValue().toInteger());
        if (aSN1Constants$EUpdateScriptTypeFindImageBinaryType.equals(ASN1Constants$EUpdateScriptType.IGNORE)) {
            return null;
        }
        String string = ((Asn1String) asn1Sequence.get(1)).getString();
        int integer = ((Asn1Integer) asn1Sequence.get(2)).getInteger();
        LinkedList linkedList = new LinkedList();
        Iterator it = ((Asn1Sequence) asn1Sequence.get(3)).getChildren().iterator();
        while (it.hasNext()) {
            linkedList.add(Utils.byteArrayToHexString(((Asn1Encodable) it.next()).getValue().toByteArray()));
        }
        return new UpdateScript(aSN1Constants$EUpdateScriptTypeFindImageBinaryType, string, integer, linkedList);
    }

    private static ASN1Constants$EUpdateScriptType findImageBinaryType(int i) {
        if (i == 0) {
            return ASN1Constants$EUpdateScriptType.PREFLIGHT;
        }
        if (i == 1) {
            return ASN1Constants$EUpdateScriptType.IMAGE;
        }
        if (i == 2) {
            return ASN1Constants$EUpdateScriptType.POSTFLIGHT;
        }
        if (i == 3) {
            return ASN1Constants$EUpdateScriptType.IMAGE_NO_RESET;
        }
        return ASN1Constants$EUpdateScriptType.IGNORE;
    }

    public List getApdus() {
        return this.apdus;
    }

    public Integer getAPDUsSizeBytes() {
        Iterator it = getApdus().iterator();
        int length = 0;
        while (it.hasNext()) {
            length += ((String) it.next()).length() / 2;
        }
        return Integer.valueOf(length);
    }

    public String toString() {
        return toString(0);
    }

    public ASN1Constants$EUpdateScriptType getBinaryType() {
        return this.binaryType;
    }

    public String getDetails() {
        return this.details;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSpecificCommandIndex(int i) throws UAIException {
        String str;
        if (1 == i) {
            str = "80EA02";
        } else if (2 == i) {
            str = "80EA03";
        } else {
            throw new UAIException("Unsupported command type");
        }
        Iterator it = getApdus().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            if (((String) it.next()).startsWith(str)) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String str;
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        HashSet hashSet = new HashSet();
        hashSet.add(ASN1Constants$EUpdateScriptType.IMAGE);
        hashSet.add(ASN1Constants$EUpdateScriptType.IMAGE_NO_RESET);
        HashSet hashSet2 = new HashSet();
        hashSet2.add("D601");
        HashSet hashSet3 = new HashSet();
        hashSet3.add(ASN1Constants$EUpdateScriptType.PREFLIGHT);
        hashSet3.add(ASN1Constants$EUpdateScriptType.POSTFLIGHT);
        HashSet hashSet4 = new HashSet();
        hashSet4.add("AAC0");
        hashSet4.add("AAC1");
        for (String str2 : getApdus()) {
            if (hashSet.contains(getBinaryType()) && hashSet2.contains(str2.substring(2, 6).toUpperCase())) {
                str = " (calculated hash of 1st signed APDU: " + HexString.fromString(Utils.calculateSHA256HexString(HexString.fromString(str2).toByteArray())) + ")";
            } else if (hashSet3.contains(getBinaryType()) && hashSet4.contains(str2.substring(2, 6).toUpperCase())) {
                str = " (calculated hash of 1st signed APDU: " + HexString.fromString(Utils.calculateSHA256HexString(HexString.fromString(str2).toByteArray())) + ")";
            }
            sb.append(strRepeat);
            sb.append("UpdateScript [");
            sb.append(getIndex());
            sb.append(", ");
            sb.append(getBinaryType());
            sb.append(", ");
            sb.append(getDetails());
            sb.append(", ");
            sb.append(getApdus().size());
            sb.append(" APDUs / ");
            sb.append(getAPDUsSizeBytes());
            sb.append(" bytes]");
            sb.append(str);
            return sb.toString();
        }
        str = "";
        sb.append(strRepeat);
        sb.append("UpdateScript [");
        sb.append(getIndex());
        sb.append(", ");
        sb.append(getBinaryType());
        sb.append(", ");
        sb.append(getDetails());
        sb.append(", ");
        sb.append(getApdus().size());
        sb.append(" APDUs / ");
        sb.append(getAPDUsSizeBytes());
        sb.append(" bytes]");
        sb.append(str);
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public UpdateScript m4774clone() {
        return new UpdateScript(this.binaryType, this.details, this.index, this.apdus);
    }

    private Asn1Sequence buildObject() {
        return new Asn1Sequence(new LinkedList() { // from class: com.nxp.id.jsbl.uai.api.objects.UpdateScript.1
            private static final long serialVersionUID = 1;

            {
                add(new Asn1Enumerated(UpdateScript.this.binaryType.getDerEnumValue()));
                add(new Asn1IA5String(UpdateScript.this.details));
                add(new Asn1Integer(UpdateScript.this.index));
                Asn1Sequence asn1Sequence = new Asn1Sequence();
                Iterator it = UpdateScript.this.apdus.iterator();
                while (it.hasNext()) {
                    asn1Sequence.add(new Asn1OctetString(HexString.fromString((String) it.next())));
                }
                add(asn1Sequence);
            }
        });
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }
}
