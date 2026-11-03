package com.mediatek.internal.telephony;

import android.content.Intent;
import android.hardware.radio.V1_2.CellInfo;
import android.hardware.radio.V1_2.CellInfoGsm;
import android.hardware.radio.V1_2.CellInfoLte;
import android.hardware.radio.V1_2.CellInfoWcdma;
import android.hardware.radio.V1_2.NetworkScanResult;
import android.hardware.radio.V1_5.CellInfoNr;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.RadioIndication;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioIndication extends RadioIndication {
    private static final String TAG = "MtkRadioInd";
    private MtkRIL mMtkRil;

    MtkRadioIndication(RIL ril) {
        super(ril);
        this.mMtkRil = (MtkRIL) ril;
    }

    public void rilConnected(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLog(1034);
        this.mMtkRil.notifyRegistrantsRilConnectionChanged(15);
    }

    public void radioStateChanged(int indicationType, int radioState) {
        int oldState = this.mMtkRil.getRadioState();
        super.radioStateChanged(indicationType, radioState);
        int newState = this.mMtkRil.getRadioState();
        if (newState != oldState) {
            Intent intent = new Intent("com.mediatek.intent.action.RADIO_STATE_CHANGED");
            int transferedState = RILUtils.convertHalRadioState(radioState);
            intent.putExtra("radioState", transferedState);
            intent.putExtra("subId", MtkSubscriptionManager.getSubIdUsingPhoneId(this.mMtkRil.mInstanceId.intValue()));
            this.mMtkRil.mMtkContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            this.mMtkRil.riljLog("Broadcast for RadioStateChanged: state=" + transferedState);
        }
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void networkScanResult_1_2(int indicationType, NetworkScanResult result) {
        boolean showRat = MtkRIL.showRat;
        Iterator<CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            CellInfo record = it.next();
            switch (record.cellInfoType) {
                case 1:
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) record.gsm.get(0);
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.mcc + cellInfoGsm.cellIdentityGsm.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.lac;
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac);
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (showRat) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    CellInfoLte cellInfoLte = (CellInfoLte) record.lte.get(0);
                    mccmnc = cellInfoLte.cellIdentityLte.base.mcc + cellInfoLte.cellIdentityLte.base.mnc;
                    int nLac2 = cellInfoLte.cellIdentityLte.base.tac;
                    cellInfoLte.cellIdentityLte.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac2);
                    cellInfoLte.cellIdentityLte.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (showRat) {
                        cellInfoLte.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) record.wcdma.get(0);
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.mnc;
                    int nLac3 = cellInfoWcdma.cellIdentityWcdma.base.lac;
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac3);
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (showRat) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + record.cellInfoType);
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_2(indicationType, result);
    }

    public void networkScanResult_1_4(int indicationType, android.hardware.radio.V1_4.NetworkScanResult result) {
        boolean showRat = MtkRIL.showRat;
        Iterator<android.hardware.radio.V1_4.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_4.CellInfo record = it.next();
            switch (record.info.getDiscriminator()) {
                case 0:
                    CellInfoGsm cellInfoGsm = record.info.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.mcc + cellInfoGsm.cellIdentityGsm.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.lac;
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac);
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (showRat) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    break;
                case 2:
                    CellInfoWcdma cellInfoWcdma = record.info.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.lac;
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac2);
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (showRat) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 3:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.info.getDiscriminator()));
                case 4:
                    android.hardware.radio.V1_4.CellInfoLte cellInfoLte = record.info.lte();
                    mccmnc = cellInfoLte.base.cellIdentityLte.base.mcc + cellInfoLte.base.cellIdentityLte.base.mnc;
                    int nLac3 = cellInfoLte.base.cellIdentityLte.base.tac;
                    cellInfoLte.base.cellIdentityLte.operatorNames.alphaLong = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac3);
                    cellInfoLte.base.cellIdentityLte.operatorNames.alphaShort = this.mMtkRil.lookupOperatorName(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (showRat) {
                        cellInfoLte.base.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.base.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.base.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.base.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_4(indicationType, result);
    }

    public void networkScanResult_1_5(int indicationType, android.hardware.radio.V1_5.NetworkScanResult result) {
        boolean showRat = MtkRIL.showRat;
        Iterator<android.hardware.radio.V1_5.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_5.CellInfo record = it.next();
            switch (record.ratSpecificInfo.getDiscriminator()) {
                case 0:
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsm = record.ratSpecificInfo.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.base.mcc + cellInfoGsm.cellIdentityGsm.base.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.base.lac;
                    cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac);
                    cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (showRat) {
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.base.lac;
                    cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac2);
                    cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (showRat) {
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.ratSpecificInfo.getDiscriminator()));
                case 3:
                    android.hardware.radio.V1_5.CellInfoLte cellInfoLte = record.ratSpecificInfo.lte();
                    mccmnc = cellInfoLte.cellIdentityLte.base.base.mcc + cellInfoLte.cellIdentityLte.base.base.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.base.base.tac;
                    cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac3);
                    cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (showRat) {
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoNr cellInfoNr = record.ratSpecificInfo.nr();
                    mccmnc = cellInfoNr.cellIdentityNr.base.mcc + cellInfoNr.cellIdentityNr.base.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.base.tac;
                    cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nTac);
                    cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (showRat) {
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_5(indicationType, result);
    }

    public void networkScanResult_1_6(int indicationType, android.hardware.radio.V1_6.NetworkScanResult result) {
        boolean showRat = MtkRIL.showRat;
        Iterator<android.hardware.radio.V1_6.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_6.CellInfo record = it.next();
            switch (record.ratSpecificInfo.getDiscriminator()) {
                case 0:
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsm = record.ratSpecificInfo.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.base.mcc + cellInfoGsm.cellIdentityGsm.base.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.base.lac;
                    cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac);
                    cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (showRat) {
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.base.lac;
                    cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac2);
                    cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (showRat) {
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.ratSpecificInfo.getDiscriminator()));
                case 3:
                    android.hardware.radio.V1_6.CellInfoLte cellInfoLte = record.ratSpecificInfo.lte();
                    mccmnc = cellInfoLte.cellIdentityLte.base.base.mcc + cellInfoLte.cellIdentityLte.base.base.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.base.base.tac;
                    cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac3);
                    cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (showRat) {
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    android.hardware.radio.V1_6.CellInfoNr cellInfoNr = record.ratSpecificInfo.nr();
                    mccmnc = cellInfoNr.cellIdentityNr.base.mcc + cellInfoNr.cellIdentityNr.base.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.base.tac;
                    cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nTac);
                    cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (showRat) {
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_6(indicationType, result);
    }
}
