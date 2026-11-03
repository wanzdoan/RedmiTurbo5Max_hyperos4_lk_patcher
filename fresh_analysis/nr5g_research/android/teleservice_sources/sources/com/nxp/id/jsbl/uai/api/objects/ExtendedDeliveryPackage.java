package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.EcdsaSigValue;
import com.nxp.id.jsbl.uai.api.util.UAIAPIException;
import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Parser;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class ExtendedDeliveryPackage implements Asn1Encodable {
    private final ExtendedDeliveryPackageData extendedDeliveryPackageData;
    private HexString privateKey;
    protected HexString signature;

    public ExtendedDeliveryPackage(File file, boolean z) throws FileNotFoundException, UAIAPIException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "- File does not exist");
        }
        try {
            ExtendedDeliveryPackage extendedDeliveryPackageDecodeFromDER = decodeFromDER(Asn1Parser.parse(HexString.fromFile(file.getCanonicalFile(), true), z));
            this.extendedDeliveryPackageData = extendedDeliveryPackageDecodeFromDER.getExtendedDeliveryPackageData();
            setAll(extendedDeliveryPackageDecodeFromDER.privateKey, extendedDeliveryPackageDecodeFromDER.signature);
        } catch (UAIException e) {
            throw new UAIAPIException("Error while reading extended delivery package", e);
        }
    }

    public ExtendedDeliveryPackage(ExtendedDeliveryPackageData extendedDeliveryPackageData, HexString hexString, HexString hexString2) throws UAIException {
        Utils.checkParamNotNull(extendedDeliveryPackageData);
        if (hexString != null && hexString2 != null) {
            throw new UAIException("Exactly one of privateKey and signature has to be defined!");
        }
        this.extendedDeliveryPackageData = extendedDeliveryPackageData.m4776clone();
        if (hexString != null) {
            this.privateKey = hexString.m4777clone();
        } else if (hexString2 != null) {
            this.signature = hexString2.m4777clone();
        }
    }

    public ExtendedDeliveryPackage(ExtendedDeliveryPackageData extendedDeliveryPackageData) {
        Utils.checkParamNotNull(extendedDeliveryPackageData);
        this.extendedDeliveryPackageData = extendedDeliveryPackageData.m4776clone();
    }

    public static ExtendedDeliveryPackage decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        ExtendedDeliveryPackageData extendedDeliveryPackageDataDecodeFromDER = ExtendedDeliveryPackageData.decodeFromDER(asn1Sequence.get(0));
        if (asn1Sequence.size() == 2) {
            return new ExtendedDeliveryPackage(extendedDeliveryPackageDataDecodeFromDER, null, asn1Sequence.get(1).getValue());
        }
        return new ExtendedDeliveryPackage(extendedDeliveryPackageDataDecodeFromDER);
    }

    private void setAll(HexString hexString, HexString hexString2) {
        if (hexString != null) {
            this.privateKey = hexString.m4777clone();
        } else if (hexString2 != null) {
            this.signature = hexString2.m4777clone();
        }
    }

    public DeliveryPackage getDeliveryPackage() {
        return this.extendedDeliveryPackageData.getDeliveryPackage();
    }

    public CustomerControlImage getCustomerControlImage() {
        return this.extendedDeliveryPackageData.getCustomerControlImage();
    }

    public String toString() {
        return toString(0);
    }

    public ExtendedDeliveryPackageData getExtendedDeliveryPackageData() {
        return this.extendedDeliveryPackageData;
    }

    private Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(this.extendedDeliveryPackageData);
        if (this.signature != null) {
            linkedList.add(new Asn1OctetString(this.signature));
        }
        return new Asn1Sequence(linkedList);
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
        return strRepeat + "ExtendedDeliveryPackage \n" + this.extendedDeliveryPackageData.toString(i + 2) + "\n" + strRepeat + "  Signature [" + this.signature + "]\n" + strRepeat + "End of ExtendedDeliveryPackage";
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ExtendedDeliveryPackage m4768clone() {
        try {
            return new ExtendedDeliveryPackage(this.extendedDeliveryPackageData, this.privateKey, this.signature);
        } catch (UAIException unused) {
            return null;
        }
    }

    public byte[] getSignaturePlain() throws UAIAPIException {
        HexString hexString = this.signature;
        if (hexString == null) {
            return null;
        }
        try {
            EcdsaSigValue ecdsaSigValueDecodeFromDER = EcdsaSigValue.decodeFromDER(Asn1Parser.parse(hexString));
            return formatECDSAComponent(ecdsaSigValueDecodeFromDER.getR()).append(formatECDSAComponent(ecdsaSigValueDecodeFromDER.getS())).toByteArray();
        } catch (Exception unused) {
            throw new UAIAPIException("Invalid or no signature present in the delivery file.");
        }
    }

    public byte[] getSignaturePlainECC384() throws UAIAPIException {
        HexString hexString = this.signature;
        if (hexString == null) {
            return null;
        }
        try {
            EcdsaSigValue ecdsaSigValueDecodeFromDER = EcdsaSigValue.decodeFromDER(Asn1Parser.parse(hexString));
            return formatECDSAComponent(ecdsaSigValueDecodeFromDER.getR(), EcdsaSigValue.COMPONENT_SIZE_ECC384).append(formatECDSAComponent(ecdsaSigValueDecodeFromDER.getS(), EcdsaSigValue.COMPONENT_SIZE_ECC384)).toByteArray();
        } catch (Exception unused) {
            throw new UAIAPIException("Invalid or no signature present in the delivery file.");
        }
    }

    private HexString formatECDSAComponent(BigInteger bigInteger) {
        return formatECDSAComponent(bigInteger, EcdsaSigValue.COMPONENT_SIZE);
    }

    private HexString formatECDSAComponent(BigInteger bigInteger, int i) {
        HexString hexStringFromBigInteger = HexString.fromBigInteger(bigInteger);
        int i2 = i * 2;
        if (hexStringFromBigInteger.length() >= i2) {
            return hexStringFromBigInteger.substring(hexStringFromBigInteger.length() - i2);
        }
        return hexStringFromBigInteger.length() < i2 ? hexStringFromBigInteger.padToBlockSize(true, (byte) 0, i2) : hexStringFromBigInteger;
    }
}
