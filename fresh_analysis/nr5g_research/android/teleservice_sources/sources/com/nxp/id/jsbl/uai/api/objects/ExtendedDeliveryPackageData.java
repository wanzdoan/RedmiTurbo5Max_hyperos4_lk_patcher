package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.APDUUAI;
import com.nxp.id.jsbl.uai.api.util.ASN1Constants$EUpdateScriptType;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class ExtendedDeliveryPackageData implements Asn1Encodable {
    private final CustomerControlImage cci;
    private final DeliveryPackage deliveryPackage;
    private int version;

    private ExtendedDeliveryPackageData(int i, DeliveryPackage deliveryPackage, CustomerControlImage customerControlImage) {
        this.version = i;
        this.deliveryPackage = deliveryPackage.m4771clone();
        this.cci = customerControlImage.m4771clone();
    }

    public static ExtendedDeliveryPackageData decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        String string = ((Asn1IA5String) asn1Sequence.get(0)).getString();
        if (!string.equals("EDP")) {
            DeliveryPackage$$ExternalSyntheticBUOutline0.m("ExtendedDeliveryPackageData Tag did not match expected value. Expected EDP, but was ", string);
            return null;
        }
        return new ExtendedDeliveryPackageData(((Asn1Integer) asn1Sequence.get(1)).getInteger(), DeliveryPackage.decodeFromDER(asn1Sequence.get(2)), CustomerControlImage.decodeFromDER(asn1Sequence.get(3)));
    }

    public String toString() {
        return toString(0);
    }

    public DeliveryPackage getDeliveryPackage() {
        return this.deliveryPackage;
    }

    public CustomerControlImage getCustomerControlImage() {
        return this.cci;
    }

    private Asn1Sequence buildObject() {
        Asn1Sequence asn1Sequence = new Asn1Sequence();
        asn1Sequence.add(new Asn1IA5String("EDP"));
        asn1Sequence.add(new Asn1Integer(this.version));
        asn1Sequence.add(this.deliveryPackage);
        asn1Sequence.add(this.cci);
        return asn1Sequence;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("ExtendedDeliveryPackageData (calculated hash: ");
        sb.append(HexString.fromByteArray(getSHA256Hash()).toString());
        sb.append("\n");
        sb.append(strRepeat);
        sb.append("  Tag EDP\n");
        sb.append(strRepeat);
        sb.append("  Version ");
        sb.append(this.version);
        sb.append("\n");
        int i2 = i + 2;
        sb.append(this.deliveryPackage.toString(i2));
        sb.append("\n");
        sb.append(this.cci.toString(i2));
        sb.append("\n");
        sb.append(strRepeat);
        sb.append("End of ExtendedDeliveryPackageData");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ExtendedDeliveryPackageData m4769clone() {
        return new ExtendedDeliveryPackageData(this.version, this.deliveryPackage, this.cci);
    }

    public byte[] getSHA256Hash() {
        return Utils.calculateSHA256ByteArray(encode().toByteArray());
    }

    public byte[] getSHA384Hash() {
        return Utils.calculateSHA384ByteArray(encode().toByteArray());
    }

    public APDUUAI.osuType getOSUType() {
        byte b;
        DeliveryPackage deliveryPackage = this.deliveryPackage;
        if (deliveryPackage != null && deliveryPackage.getJCOPControlImage() != null && this.deliveryPackage.getJCOPControlImage().getJCOPControlImageData() != null && this.deliveryPackage.getJCOPControlImage().getJCOPControlImageData().getUpdateControlData() != null && this.deliveryPackage.getJCOPControlImage().getJCOPControlImageData().getUpdateControlData().getUpdateControlCaseList() != null && this.deliveryPackage.getJCOPControlImage().getJCOPControlImageData().getUpdateControlData().getUpdateControlCaseList().size() > 0) {
            boolean z = false;
            boolean z2 = false;
            for (UpdateControlCaseFormatLV updateControlCaseFormatLV : this.deliveryPackage.getJCOPControlImage().getJCOPControlImageData().getUpdateControlData().getUpdateControlCaseList()) {
                Iterator it = updateControlCaseFormatLV.getOSID().iterator();
                while (it.hasNext()) {
                    byte bByteValue = ((Integer) it.next()).byteValue();
                    int iIntValue = ((Integer) updateControlCaseFormatLV.getFSN().get(0)).intValue();
                    if (17 == bByteValue) {
                        z = true;
                    }
                    if (iIntValue == ((Integer) updateControlCaseFormatLV.getCSN().get(0)).intValue() && iIntValue == ((Integer) updateControlCaseFormatLV.getRSN().get(0)).intValue()) {
                        z = true;
                    }
                    if (90 == bByteValue) {
                        z2 = true;
                    }
                }
            }
            if (z) {
                if (z2) {
                    return APDUUAI.osuType.OS_TYPE_SELF_UPDATE_WITH_AMDH;
                }
                return APDUUAI.osuType.OS_TYPE_SELF_UPDATE;
            }
            if (z2) {
                return APDUUAI.osuType.OS_TYPE_REAL_UPDATE_WITH_AMDH;
            }
            DeliveryPackage deliveryPackage2 = this.deliveryPackage;
            if (deliveryPackage2 != null && deliveryPackage2.getUpdateScripts() != null && this.deliveryPackage.getUpdateScripts().getUpdateScriptList() != null && this.deliveryPackage.getUpdateScripts().getUpdateScriptList().size() > 0) {
                Iterator it2 = this.deliveryPackage.getUpdateScripts().getUpdateScriptList().iterator();
                boolean z3 = false;
                boolean z4 = false;
                while (it2.hasNext()) {
                    byte[] bArrHexStringToByteArray = Utils.hexStringToByteArray((String) ((UpdateScript) it2.next()).getApdus().get(0));
                    if (bArrHexStringToByteArray != null && bArrHexStringToByteArray.length >= 4) {
                        if (bArrHexStringToByteArray[0] == -128 && bArrHexStringToByteArray[1] == -42 && bArrHexStringToByteArray[2] == 1 && bArrHexStringToByteArray[3] == 2 && ((b = bArrHexStringToByteArray[4]) == -63 || b == -1)) {
                            z3 = true;
                        } else {
                            z4 = true;
                        }
                    }
                }
                if (z3 && z4) {
                    return APDUUAI.osuType.OS_TYPE_REAL_PATCH_UPDATE;
                }
                if (z3 && !z4) {
                    return APDUUAI.osuType.OS_TYPE_PATCH_UPDATE;
                }
                return APDUUAI.osuType.OS_TYPE_REAL_UPDATE;
            }
            return APDUUAI.osuType.OS_TYPE_REAL_UPDATE;
        }
        return APDUUAI.osuType.OS_TYPE_UNKNOWN;
    }

    public boolean isResetRequiredAfterAction(int i) {
        return getDeliveryPackage().getUpdateScripts().getUpdateScript(i).getBinaryType().compareTo(ASN1Constants$EUpdateScriptType.IMAGE_NO_RESET) != 0;
    }
}
