package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.APDUUAI;
import com.nxp.id.jsbl.uai.api.util.JSBLExceptionExternal;
import com.nxp.id.jsbl.uai.api.util.UAIAPIException;
import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class JCOPControlImage implements Asn1Encodable {
    private Integer errInjectionCorruptedLen;
    private final JCOPControlImageData jCOPControlImageData;
    private HexString privateKey;
    private HexString signature;

    private JCOPControlImage(JCOPControlImageData jCOPControlImageData, HexString hexString, HexString hexString2, Integer num) {
        this.signature = null;
        this.privateKey = null;
        this.errInjectionCorruptedLen = null;
        this.jCOPControlImageData = jCOPControlImageData.m4773clone();
        if (hexString != null) {
            this.signature = hexString.m4777clone();
        }
        if (hexString2 != null) {
            this.privateKey = hexString2.m4777clone();
        }
        this.errInjectionCorruptedLen = num;
    }

    public JCOPControlImage(JCOPControlImageData jCOPControlImageData, HexString hexString) {
        this.signature = null;
        this.privateKey = null;
        this.errInjectionCorruptedLen = null;
        this.jCOPControlImageData = jCOPControlImageData.m4773clone();
        this.signature = hexString;
    }

    public static JCOPControlImage decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1Sequence)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1Sequence", asn1Encodable.getClass()));
        }
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        JCOPControlImageData jCOPControlImageDataDecodeFromDER = JCOPControlImageData.decodeFromDER(asn1Sequence.get(0));
        if (asn1Sequence.size() == 1) {
            return new JCOPControlImage(jCOPControlImageDataDecodeFromDER, null);
        }
        return new JCOPControlImage(jCOPControlImageDataDecodeFromDER, asn1Sequence.get(1).getValue());
    }

    public UpdateControlData getUpdateControlData() {
        return this.jCOPControlImageData.getUpdateControlData();
    }

    public List toApdu(int i, boolean z) throws UAIAPIException {
        try {
            return toApdu(APDUUAI.outputSyntax.APDU, i, z);
        } catch (JSBLExceptionExternal e) {
            throw new UAIAPIException(e.getMessage(), e);
        }
    }

    public String toString() {
        return toString(0);
    }

    public JCOPControlImageData getJCOPControlImageData() {
        return this.jCOPControlImageData;
    }

    public int getMatchingAction(int i, int i2, int i3, int i4) {
        for (UpdateControlCase updateControlCase : this.jCOPControlImageData.getUpdateControlData().getUpdateControlCaseList()) {
            if (updateControlCase.getOSID().contains(Integer.valueOf(i)) && updateControlCase.getRSN().contains(Integer.valueOf(i2)) && updateControlCase.getCSN().contains(Integer.valueOf(i3))) {
                if (!updateControlCase.getFSN().contains(Integer.valueOf(i4)) || i4 == -1) {
                    if (i4 == -1) {
                        Integer actionIndex = updateControlCase.getActionIndex();
                        if (actionIndex == null) {
                            break;
                        }
                        return actionIndex.intValue();
                    }
                } else if (((Integer) Collections.max(updateControlCase.getFSN())).intValue() > -1) {
                    Integer actionIndex2 = updateControlCase.getActionIndex();
                    if (actionIndex2 == null) {
                        break;
                    }
                    return actionIndex2.intValue();
                }
            }
        }
        return -1;
    }

    public List toApdu(APDUUAI.outputSyntax outputsyntax, int i, boolean z) {
        return APDUUAI.byteArrayToUAIAPDU(encode().toByteArray(), outputsyntax, (byte) -69, Integer.valueOf(i), z);
    }

    public Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(this.jCOPControlImageData);
        if (this.signature != null) {
            linkedList.add(new Asn1OctetString(this.signature));
        }
        return new Asn1Sequence(linkedList);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        HexString hexStringEncode = buildObject().encode();
        Integer num = this.errInjectionCorruptedLen;
        return num != null ? hexStringEncode.replaceAt(1, HexString.fromByte(num.byteValue())) : hexStringEncode;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        return strRepeat + "JCOPControlImage (calculated hash: " + HexString.fromString(Utils.calculateSHA256HexString(encode().toByteArray())) + ")\n" + this.jCOPControlImageData.toString(i + 2) + "\n" + strRepeat + "  Signature [" + this.signature + "]\n" + strRepeat + "End of JCOPControlImage";
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public JCOPControlImage m4770clone() {
        return new JCOPControlImage(this.jCOPControlImageData, this.signature, this.privateKey, this.errInjectionCorruptedLen);
    }
}
