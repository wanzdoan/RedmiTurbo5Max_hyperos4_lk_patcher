package com.mediatek.internal.telephony;

import android.hardware.radio.network.CellInfo;
import android.hardware.radio.network.CellInfoGsm;
import android.hardware.radio.network.CellInfoLte;
import android.hardware.radio.network.CellInfoNr;
import android.hardware.radio.network.CellInfoWcdma;
import android.hardware.radio.network.NetworkScanResult;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.NetworkIndication;

/* JADX INFO: loaded from: classes.dex */
public class MtkNetworkIndication extends NetworkIndication {
    private MtkRIL mMtkRil;

    public MtkNetworkIndication(MtkRIL ril) {
        super(ril);
        this.mMtkRil = ril;
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void networkScanResult(int indicationType, NetworkScanResult result) {
        boolean showRat = MtkRIL.showRat;
        for (CellInfo record : result.networkInfos) {
            String mccmnc = null;
            switch (record.ratSpecificInfo.getTag()) {
                case 0:
                    CellInfoGsm cellInfoGsm = record.ratSpecificInfo.getGsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.mcc + cellInfoGsm.cellIdentityGsm.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.lac;
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac);
                    cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (showRat) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.getWcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.mcc + cellInfoWcdma.cellIdentityWcdma.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.lac;
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac2);
                    cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (showRat) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + record.ratSpecificInfo.getTag());
                case 3:
                    CellInfoLte cellInfoLte = record.ratSpecificInfo.getLte();
                    mccmnc = cellInfoLte.cellIdentityLte.mcc + cellInfoLte.cellIdentityLte.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.tac;
                    cellInfoLte.cellIdentityLte.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nLac3);
                    cellInfoLte.cellIdentityLte.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (showRat) {
                        cellInfoLte.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoNr cellInfoNr = record.ratSpecificInfo.getNr();
                    mccmnc = cellInfoNr.cellIdentityNr.mcc + cellInfoNr.cellIdentityNr.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.tac;
                    cellInfoNr.cellIdentityNr.operatorNames.alphaLong = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, true, nTac);
                    cellInfoNr.cellIdentityNr.operatorNames.alphaShort = this.mMtkRil.lookupOperatorNameForPlmnList(getSubId(this.mMtkRil.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (showRat) {
                        cellInfoNr.cellIdentityNr.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult(indicationType, result);
    }
}
